package uk.co.rustynailor.widewords;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import uk.co.rustynailor.widewords.models.Quiz;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView resultsView = (TextView) findViewById(R.id.resultsContainer);

        Quiz quiz = (Quiz) getIntent().getParcelableExtra("quiz");

        int correctCount = 0;
        for(int answer : quiz.getQuizQuestionResults()){
            if(answer == 1)
                correctCount++;
        }

        resultsView.setText("You got " + correctCount + "/" + quiz.getQuizQuestionResults().size() + " answers correct");
    }

}
