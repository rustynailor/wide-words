package uk.co.rustynailor.widewords;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.utilities.QuizManager;

public class QuizQuestionActivity extends AppCompatActivity  implements LoaderCallbacks<Cursor> {

    private Quiz mQuiz;
    private TextView mWord;
    private ArrayList<TextView> mAnswers;
    private int mCorrectAnswerPosition;
    private Chronometer mCountdown;
    private QuizQuestionActivity mContext;

    private static final int QUESTION_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_quiz_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialise textviews
        mWord = (TextView) findViewById(R.id.word);
        TextView answer1 = (TextView) findViewById(R.id.answer_1);
        TextView answer2 = (TextView) findViewById(R.id.answer_2);
        TextView answer3 = (TextView) findViewById(R.id.answer_3);
        TextView answer4 = (TextView) findViewById(R.id.answer_4);
        mAnswers = new ArrayList<>();
        mAnswers.add(answer1);
        mAnswers.add(answer2);
        mAnswers.add(answer3);
        mAnswers.add(answer4);

        mCountdown = (Chronometer) findViewById(R.id.countdown);

        mQuiz = (Quiz) getIntent().getParcelableExtra("quiz");
        getSupportLoaderManager().initLoader(QUESTION_LOADER_ID, null, this);

        mCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if we can advance to the next question do so, else exit
                if(mQuiz.nextQuestion() == false){
                    finish();
                } else {
                    getSupportLoaderManager().restartLoader(QUESTION_LOADER_ID, null, mContext);
                }
            }
        });


    }

    private void populateQuiz(Cursor cursor){

        Random randomGenerator = new Random();
        //randomly set position of correct word
        mCorrectAnswerPosition = randomGenerator.nextInt(3);

        mWord.setText(cursor.getString(ColumnProjections.COL_QQ_WORD));

        mAnswers.get(mCorrectAnswerPosition).setText(cursor.getString(ColumnProjections.COL_QQ_DEFINITION));

        ArrayList<String> wrongDefinitions = new ArrayList<>();
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_1));
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_2));
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_3));

        //loop through answers setting up textview
        for(int i=0; i<mAnswers.size(); i++){

            if(i!=mCorrectAnswerPosition) {
                mAnswers.get(i).setText(wrongDefinitions.get(0));
                wrongDefinitions.remove(0);
            }
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader loader =  new CursorLoader(
                this,   // Parent activity context
                WideWordsProvider.QuizQuestion.withId(mQuiz.getCurrentQuizQuestionId()),        // Table to query
                ColumnProjections.QUIZ_QUESTION_COLUMNS,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst()){
            populateQuiz(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
