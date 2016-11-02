package uk.co.rustynailor.widewords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.utilities.QuizManager;

public class QuizQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Quiz  quiz = (Quiz) getIntent().getParcelableExtra("quiz");



    }

}
