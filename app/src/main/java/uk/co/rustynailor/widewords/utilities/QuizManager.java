package uk.co.rustynailor.widewords.utilities;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;
import uk.co.rustynailor.widewords.enums.QuizQuestionResult;
import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.models.QuizQuestion;
import uk.co.rustynailor.widewords.models.Word;

/**
 * Created by russellhicks on 29/10/2016.
 */

public class QuizManager {

    //projections
    public static final String[] WORD_COLUMNS = {
            WideWordsDatabase.WORDS + "." + WordColumns._ID,
            WideWordsDatabase.WORDS + "." + WordColumns.DEFINITION,
            WideWordsDatabase.WORDS + "." + WordColumns.WORD
    };


    // These indices are tied to WORD_COLUMNS.  If WORD_COLUMNS change, these
    // must change.
    public static final int COL_WORD_ID = 0;
    public static final int COL_WORD_DEFINITION = 1;
    public static final int COL_WORD_WORD = 2;

    //array to hold all words
    ArrayList<Word> words = new ArrayList<>();
    //array to hold the chosen words
    ArrayList<Word> chosenWords = new ArrayList<>();
    Random randomGenerator = new Random();


    //build a new ten question Quiz
    public Quiz buildQuiz(Context context){
        Quiz quiz = new Quiz();


        //first get number of unanswered questions
        String query =  WideWordsDatabase.WORDS + "." + WordColumns.CORRECT_COUNT + " <  ?";
        String[] params =  {"3"};
        Cursor c = context.getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                WORD_COLUMNS, query, params, null);
        Log.i("QuizManager", "Words to master: " + c.getCount());

        //load cursor into arraylist of unanswered words
        while (c.moveToNext()){
            Word word = new Word();
            word.setId(c.getInt(COL_WORD_ID));
            word.setWord(c.getString(COL_WORD_WORD));
            word.setDefinition(c.getString(COL_WORD_DEFINITION));

            //add word to array
            words.add(word);
        }

        //number of questions in quiz
        int questionCount = (c.getCount() >= 10) ?  10 : c.getCount();
        //TODO: questioncount == 0...

        c.close();

        //pick our test questions;
        for(int i=0; i < questionCount; i++){
            Word selectedWord = null;
            while(selectedWord == null){
                int wordToGet = randomGenerator.nextInt(words.size());
                //if we haven't already selected it, add to array of selected words
                if(!chosenWords.contains(words.get(wordToGet))){
                    selectedWord = words.get(wordToGet);
                }
            }
            chosenWords.add(selectedWord);
        }

        for(int questionPointer = 0; questionPointer < questionCount; questionCount++){

            //pull together questions
            QuizQuestion quizQuestion = new QuizQuestion();
            Word chosenWord = chosenWords.get(questionPointer);

            quizQuestion.setPosition(questionPointer + 1);
            quizQuestion.setWordId(chosenWord.getId());
            quizQuestion.setWord(chosenWord.getWord());
            quizQuestion.setDefinition(chosenWord.getDefinition());

            ArrayList usedWords = new ArrayList<Word>();
            usedWords.add(chosenWord);


            //get wrong answers
            for(int wp=1; wp <=3; wp++) {
                Word wrongWord = getWrongWord(usedWords);
                usedWords.add(wrongWord);
                if(wp == 1) {
                    quizQuestion.setWrongDefinition1(wrongWord.getDefinition());
                    quizQuestion.setWrongDefinition1Id(wrongWord.getId());
                } else if(wp == 2) {
                    quizQuestion.setWrongDefinition2(wrongWord.getDefinition());
                    quizQuestion.setWrongDefinition2Id(wrongWord.getId());
                } else if(wp == 3) {
                    quizQuestion.setWrongDefinition3(wrongWord.getDefinition());
                    quizQuestion.setWrongDefinition3Id(wrongWord.getId());
                }
            }

            quizQuestion.setQuizQuestionResult(QuizQuestionResult.QUEUE);

            //add question to quiz
            quiz.addQuizQuestion(quizQuestion);

        } //end quiz question loop

        return quiz;

    }

    private Word getWrongWord(ArrayList<Word> usedWords){
        Word selectedWord = null;
        while(selectedWord == null){
            int wordToGet = randomGenerator.nextInt(words.size());
            //if we haven't already selected it, add to array of selected words
            if(!usedWords.contains(words.get(wordToGet))){
                selectedWord = words.get(wordToGet);
            }
        }
        return selectedWord;
    }

}
