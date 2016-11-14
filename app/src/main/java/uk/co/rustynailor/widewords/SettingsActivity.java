package uk.co.rustynailor.widewords;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uk.co.rustynailor.widewords.utilities.QuizManager;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button resetDatabase = (Button) findViewById(R.id.reset_words);
        resetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizManager quizManager = new QuizManager();
                quizManager.resetCounts(mContext);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(mContext, R.string.word_counts_reset, duration);
                toast.show();
            }
        });

        Button downloadNewData = (Button) findViewById(R.id.download_words);
        downloadNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });


    }

}
