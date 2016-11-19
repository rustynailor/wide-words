package uk.co.rustynailor.widewords;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import org.w3c.dom.Text;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.models.LearnList;
import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.models.Word;

public class LearnWordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WORD_LOADER_ID = 1;

    private LearnList mLearnList;

    private TextView mWord;
    private TextView mDefinition;

    private TextView mFinish;
    private TextView mNext;
    private TextView mPrevious;

    private LearnWordActivity mContext;
    private ShareActionProvider mShareActionProvider;


    public static final String TAG = "LearnWordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_learn_word);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWord = (TextView) findViewById(R.id.word_container);
        mDefinition = (TextView) findViewById(R.id.definition_container);

        mFinish = (TextView) findViewById(R.id.finish_button);
        mNext = (TextView) findViewById(R.id.next_button);
        mPrevious = (TextView) findViewById(R.id.back_button);

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLearnList.nextWord()){
                    getSupportLoaderManager().restartLoader(WORD_LOADER_ID, null, mContext);
                }
            }
        });

        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLearnList.previousWord()){
                    getSupportLoaderManager().restartLoader(WORD_LOADER_ID, null, mContext);
                }
            }
        });

        mLearnList = getIntent().getParcelableExtra(getString(R.string.learn_list));
        getSupportLoaderManager().initLoader(WORD_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        //handle first load if content provider finishes first
        if(!mWord.getText().equals("")){
            setShareIntent();
        }

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent() {
        if (mShareActionProvider != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_intent_string, mWord.getText(), mDefinition.getText()));
            sendIntent.setType("text/plain");
            mShareActionProvider.setShareIntent(sendIntent);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader loader =  new CursorLoader(
                this,   // Parent activity context
                WideWordsProvider.Words.withId(mLearnList.getCurrentWordId()),        // Table to query
                ColumnProjections.WORD_COLUMNS,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst()){
            //do something with data
            mWord.setText(data.getString(ColumnProjections.COL_WORD_WORD));
            mDefinition.setText(data.getString(ColumnProjections.COL_WORD_DEFINITION));
            //update share intent
            setShareIntent();
        }
        //if at start, hide back button
        if(mLearnList.getWordListPosition() == 0){
            mPrevious.setVisibility(View.INVISIBLE);
        } else {
            mPrevious.setVisibility(View.VISIBLE);
        }

        //if at last position, hide next button
        if(mLearnList.getWordListPosition() == mLearnList.getWordList().size()-1){
            mNext.setVisibility(View.INVISIBLE);
        } else {
            mNext.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
