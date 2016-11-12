package uk.co.rustynailor.widewords;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;
import uk.co.rustynailor.widewords.enums.QuizQuestionResult;
import uk.co.rustynailor.widewords.models.LearnList;
import uk.co.rustynailor.widewords.utilities.QuizManager;

import static uk.co.rustynailor.widewords.data.ColumnProjections.WORD_COLUMNS;

public class LearnActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //loader constant
    private static final int ALL_WORDS_LOADER_ID = 1;

    private Button mNewWords;
    private Button mAllWords;
    private Button mMasteredWords;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        //if we are offering a choice, set content view
        setContentView(R.layout.activity_learn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get buttons
        mNewWords = (Button) findViewById(R.id.new_words);
        mAllWords = (Button) findViewById(R.id.all_words);
        mMasteredWords = (Button) findViewById(R.id.mastered_words);

        mNewWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz(QuizManager.LEARN_TYPE_NEW);
            }
        });

        mAllWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz(QuizManager.LEARN_TYPE_ALL);
            }
        });

        mMasteredWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz(QuizManager.LEARN_TYPE_MASTERED);
            }
        });

        //do we have mastered words? Check database...
        getSupportLoaderManager().initLoader(ALL_WORDS_LOADER_ID, null, this);
    }


    //build learn list (using background thread)
    private void startQuiz(final int learnType){
            new Thread(new Runnable() {
                public void run() {
                    QuizManager quizManager = new QuizManager();
                    LearnList list = quizManager.buildList(mContext, learnType);
                    Intent intent = new Intent(mContext, LearnWordActivity.class);
                    intent.putExtra(getString(R.string.learn_list), list);
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

        mAllWords.setText(getString(R.string.all_words_with_count, totalWords));
        mAllWords.setVisibility(View.VISIBLE);

        //if there are new words and at least 1 mastered word, show new words button
        //(otherwise there is no point, as this is just all words)
        if(newWords > 0 && masteredCount > 1){
            mNewWords.setText(getString(R.string.new_words_with_count, newWords));
            mNewWords.setVisibility(View.VISIBLE);
        }

        if(masteredCount > 1 && masteredCount != totalWords){
            mMasteredWords.setText(getString(R.string.mastered_words_with_count, masteredCount));
            mMasteredWords.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
