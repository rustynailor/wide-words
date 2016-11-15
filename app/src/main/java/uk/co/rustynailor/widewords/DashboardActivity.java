package uk.co.rustynailor.widewords;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Build;
import android.os.RemoteException;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.w3c.dom.Text;

import java.util.ArrayList;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.InitialData;
import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;

import static uk.co.rustynailor.widewords.data.ColumnProjections.WORD_COLUMNS;

public class DashboardActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //loader constant
    private static final int ALL_WORDS_LOADER_ID = 1;

    private TextView mWelcomeText;
    private LinearLayout mProgressBannner;
    private TextView mProgressCount;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialise ads
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9110651366020121~1522761095");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mWelcomeText = (TextView) findViewById(R.id.welcome_text);
        mProgressBannner = (LinearLayout) findViewById(R.id.progressBanner);
        mProgressCount = (TextView) findViewById(R.id.progressCount);

        FrameLayout startQuiz = (FrameLayout) findViewById(R.id.start_quiz);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(intent);
            }
        });


        FrameLayout startLearn = (FrameLayout) findViewById(R.id.start_learning);
        startLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LearnActivity.class);
                startActivity(intent);
            }
        });


        //do we have mastered words? Check database...
        getSupportLoaderManager().initLoader(ALL_WORDS_LOADER_ID, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item
                Intent intent = new Intent(mContext, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

        if(masteredCount == 0){
            mWelcomeText.setText(fromHtml(getString(R.string.welcome_message, totalWords)));
            mProgressBannner.setVisibility(View.INVISIBLE);
            mWelcomeText.setVisibility(View.VISIBLE);
        } else if(masteredCount < totalWords){
            mProgressCount.setText(getString(R.string.in_progress_message, masteredCount, totalWords));
            mProgressBannner.setVisibility(View.VISIBLE);
            mWelcomeText.setVisibility(View.INVISIBLE);
        } else {
            mWelcomeText.setText(fromHtml(getString(R.string.completion_message, totalWords)));
            mProgressBannner.setVisibility(View.INVISIBLE);
            mWelcomeText.setVisibility(View.VISIBLE);
        }

    }

    //used to provide inline formatting - based on SO post:
    //http://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
