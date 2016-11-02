package uk.co.rustynailor.widewords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import uk.co.rustynailor.widewords.models.Quiz;
import uk.co.rustynailor.widewords.utilities.QuizManager;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        QuizManager quizManager = new QuizManager();
        Quiz quiz = quizManager.buildQuiz(this);

        //Todo: resume underway quiz (show options - resume or new)

        //bundle quiz ans pass to quiz questions
        Log.d("TEST", "Quiz created");

        Intent intent = new Intent(this, QuizQuestionActivity.class);
        intent.putExtra("quiz", quiz);
        startActivity(intent);
    }

}