package uk.co.rustynailor.widewords.utilities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.QuizColumns;
import uk.co.rustynailor.widewords.data.QuizQuestionColumns;
import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;
import uk.co.rustynailor.widewords.enums.QuizQuestionResult;
import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.models.QuizQuestion;
import uk.co.rustynailor.widewords.models.Word;

import static uk.co.rustynailor.widewords.data.ColumnProjections.COL_WORD_DEFINITION;
import static uk.co.rustynailor.widewords.data.ColumnProjections.COL_WORD_ID;
import static uk.co.rustynailor.widewords.data.ColumnProjections.COL_WORD_WORD;
import static uk.co.rustynailor.widewords.data.ColumnProjections.WORD_COLUMNS;

/**
 * Created by russellhicks on 29/10/2016.
 */

public class QuizManager {

    //array to hold all words
    ArrayList<Word> words = new ArrayList<>();
    //array to hold the chosen words
    ArrayList<Word> chosenWords = new ArrayList<>();
    Random randomGenerator = new Random();

    public static int NUMBER_OF_QUIZ_QUESTIONS = 5;


    //build a new ten question Quiz
    public Quiz buildQuiz(Context context){

        Quiz quiz = new Quiz();

        quiz.setQuestionPosition(0);
        Date d = new Date();
        quiz.setStarted(d.getTime());


        //save quiz to db to get Id
        ContentValues quizValues = new ContentValues();

        quizValues.put(QuizColumns.QUESTION_POSITION, quiz.getQuestionPosition());
        quizValues.put(QuizColumns.STARTED, quiz.getStarted());

        Uri newQuizUri = context.getContentResolver().insert(
                WideWordsProvider.Quiz.CONTENT_URI,
                quizValues
        );

        //get new quiz Id
        quiz.setId((int) ContentUris.parseId(newQuizUri));

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
        int questionCount = (c.getCount() >= NUMBER_OF_QUIZ_QUESTIONS ?  NUMBER_OF_QUIZ_QUESTIONS : c.getCount());
        //TODO: questioncount == 0...

        c.close();

        //pick our test questions;
        for(int i=0; i < questionCount; i++){
            //add empty answered flag
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

        for(int questionPointer = 0; questionPointer < questionCount; questionPointer++){

            //pull together questions
            QuizQuestion quizQuestion = new QuizQuestion();
            Word chosenWord = chosenWords.get(questionPointer);

            quizQuestion.setQuizId(quiz.getId());
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

            //set status to QUEUE - ie, unanswered
            quizQuestion.setQuizQuestionResult(QuizQuestionResult.QUEUE);

            //write quiz question to database
            ContentValues newQuestion = new ContentValues();

            //add data to content values
            newQuestion.put(QuizQuestionColumns.POSITION, quizQuestion.getPosition());
            newQuestion.put(QuizQuestionColumns.QUIZ_ID, quizQuestion.getQuizId());
            newQuestion.put(QuizQuestionColumns.WORD_ID, quizQuestion.getWordId());
            newQuestion.put(QuizQuestionColumns.WRONG_DEFINITION_1_ID, quizQuestion.getWrongDefinition1Id());
            newQuestion.put(QuizQuestionColumns.WRONG_DEFINITION_2_ID, quizQuestion.getWrongDefinition2Id());
            newQuestion.put(QuizQuestionColumns.WRONG_DEFINITION_3_ID, quizQuestion.getWrongDefinition3Id());
            newQuestion.put(QuizQuestionColumns.QUIZ_QUESTION_RESULT, quizQuestion.getQuizQuestionResult().toString());


            Uri newQuizQuestionUri = context.getContentResolver().insert(
                    WideWordsProvider.QuizQuestion.CONTENT_URI,
                    newQuestion
            );

            //get new quiz Id
            quizQuestion.setId((int) ContentUris.parseId(newQuizQuestionUri));

            //add question to quiz
            quiz.addQuizQuestionId(quizQuestion.getId());
            //add result pointer set to 0
            quiz.getQuizQuestionResults().add(0);
            quiz.getQuizQuestionIncorrectCount().add(0);
            quiz.getQuizQuestionCorrectCount().add(0);

        } //end quiz question loop

        return quiz;

    }

    //TODO: method to restore quiz from database

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
