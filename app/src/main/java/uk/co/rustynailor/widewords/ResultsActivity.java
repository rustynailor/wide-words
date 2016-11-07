package uk.co.rustynailor.widewords;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;
import uk.co.rustynailor.widewords.enums.QuizQuestionResult;
import uk.co.rustynailor.widewords.models.Quiz;

import static uk.co.rustynailor.widewords.data.ColumnProjections.WORD_COLUMNS;

public class ResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mResultsView;
    private TextView mMasteredResultsView;
    private TextView mMasteredRemainingView;
    private TextView mFinishButton;
    private Quiz mQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mResultsView = (TextView) findViewById(R.id.results_container);
        mMasteredResultsView = (TextView) findViewById(R.id.mastered_container);
        mMasteredRemainingView = (TextView) findViewById(R.id.mastered_remaining_container);
        mFinishButton = (TextView) findViewById(R.id.finish_button);

        // exit activity when finish is pressed
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mQuiz = (Quiz) getIntent().getParcelableExtra("quiz");


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader loader =  new CursorLoader(
                this,   // Parent activity context
                WideWordsProvider.QuizQuestion.fromQuiz(mQuiz.getId()),        // Table to query
                ColumnProjections.QUIZ_QUESTION_COLUMNS,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int correctCount = 0;
        int masteredCount= 0;

        while (data.moveToNext()){

            if(data.getInt(ColumnProjections.COL_QQ_CORRECT_COUNT) >= 3){
                masteredCount++;
            }

            QuizQuestionResult quizQuestionResult = QuizQuestionResult.valueOf(data.getString(ColumnProjections.COL_QQ_RESULT));

            if(quizQuestionResult.equals(QuizQuestionResult.CORRECT)){
                correctCount++;
            }
        }

        mResultsView.setText(correctCount + "/" + mQuiz.getQuizQuestionResults().size() + "  correct");
        mMasteredResultsView.setText(masteredCount + " words mastered");

        String query =  WideWordsDatabase.WORDS + "." + WordColumns.CORRECT_COUNT + " <  ?";
        String[] params =  {"3"};
        Cursor c = getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                WORD_COLUMNS, query, params, null);
       mMasteredRemainingView.setText("You have " + c.getCount() + " words left to master");

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
