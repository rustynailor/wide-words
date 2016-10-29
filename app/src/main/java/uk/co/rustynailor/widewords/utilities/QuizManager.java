package uk.co.rustynailor.widewords.utilities;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;
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


    //build a new ten question Quiz
    public static Quiz buildQuiz(Context context){



        Quiz quiz = new Quiz();

        //first get number of unanswered questions
        String query =  WideWordsDatabase.WORDS + "." + WordColumns.CORRECT_COUNT + " <  ?";
        String[] params =  {"3"};
        Cursor c = context.getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                WORD_COLUMNS, query, params, null);
        Log.i("QuizManager", "Words to master: " + c.getCount());

        ArrayList<Word> words = new ArrayList<>();

        //load cursor into arraylist of unanswered words
        while (c.moveToNext()){
            Word word = new Word();
            word.setId(c.getInt(COL_WORD_ID));
            word.setWord(c.getString(COL_WORD_WORD));
            word.setDefinition(c.getString(COL_WORD_DEFINITION));
        }

        //number of questions in quiz
        int questionCount = (c.getCount() >= 10) ?  10 : c.getCount();
        //TODO: questioncount == 0...

        c.close();

        for(int questionPointer = 0; questionPointer < questionCount; questionCount++){

            //pull together questions
            QuizQuestion quizQuestion = new QuizQuestion();



        }




        return quiz;

    }

}
