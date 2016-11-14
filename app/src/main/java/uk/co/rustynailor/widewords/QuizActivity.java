package uk.co.rustynailor.widewords;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.models.LearnList;
import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.utilities.QuizManager;

public class QuizActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //loader constant
    private static final int ALL_WORDS_LOADER_ID = 1;
    private Context mContext;

    private Button mStartQuiz;
    private Button mResetWords;

    private TextView mInstructions;
    private TextView mCompleteInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mInstructions = (TextView)findViewById(R.id.start_instructions);
        mCompleteInstructions = (TextView)findViewById(R.id.mastered_instructions);

        mStartQuiz = (Button) findViewById(R.id.start_quiz);
        mStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartQuiz.setOnClickListener(null);
                startQuiz();
            }
        });

        mResetWords = (Button) findViewById(R.id.reset_words);
        mResetWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizManager quizManager = new QuizManager();
                quizManager.resetCounts(mContext);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(mContext, R.string.word_counts_reset, duration);
                toast.show();
                mCompleteInstructions.setVisibility(View.GONE);
                mInstructions.setVisibility(View.VISIBLE);
                mResetWords.setVisibility(View.GONE);
                mStartQuiz.setVisibility(View.VISIBLE);
            }
        });

        //do we have mastered words? Check database...
        getSupportLoaderManager().initLoader(ALL_WORDS_LOADER_ID, null, this);
    }

    //start quiz
    private void startQuiz(){
        new Thread(new Runnable() {
            public void run() {
                QuizManager quizManager = new QuizManager();
                Quiz quiz = quizManager.buildQuiz(mContext);
                Intent intent = new Intent(mContext, QuizQuestionActivity.class);
                intent.putExtra(getString(R.string.quiz), quiz);
                startActivity(intent);
                finish();
            }
        }).start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ALL_WORDS_LOADER_ID:
                // Returns a new CursorLoader of all words
                return new CursorLoader(
                        this,   // Parent activity context
                        WideWordsProvider.Words.CONTENT_URI,        // Table to query
                        ColumnProjections.WORD_COLUMNS,     // Projection to return
                        null,            // selection clause
                        null,            //selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //count words - new words, mastered words and all words
        int masteredCount = 0;
        int totalWords = data.getCount();

        while (data.moveToNext()){
            if(data.getInt(ColumnProjections.COL_WORD_CORRECT_COUNT) >= 3){
                masteredCount++;
            }
        }
        int newWords = totalWords - masteredCount;

        //only display start quiz button if there are unanswered questions
        if(newWords > 0){
            mStartQuiz.setVisibility(View.VISIBLE);
            mInstructions.setVisibility(View.VISIBLE);
        } else {
            mResetWords.setVisibility(View.VISIBLE);
            mCompleteInstructions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //cursor not used after onLoadFinished so no action required
    }
}
