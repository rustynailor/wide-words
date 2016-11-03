package uk.co.rustynailor.widewords;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.utilities.QuizManager;

public class QuizQuestionActivity extends AppCompatActivity  implements LoaderCallbacks<Cursor> {

    private Quiz mQuiz;

    private static final int QUESTION_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuiz = (Quiz) getIntent().getParcelableExtra("quiz");

        getSupportLoaderManager().initLoader(QUESTION_LOADER_ID, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader loader =  new CursorLoader(
                this,   // Parent activity context
                WideWordsProvider.QuizQuestion.withId(mQuiz.getCurrentQuizQuestionId()),        // Table to query
                QuizManager.WORD_COLUMNS,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
