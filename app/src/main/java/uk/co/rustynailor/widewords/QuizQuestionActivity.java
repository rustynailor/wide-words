package uk.co.rustynailor.widewords;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.models.Quiz;

public class QuizQuestionActivity extends AppCompatActivity  implements View.OnClickListener, LoaderCallbacks<Cursor> {

    private Quiz mQuiz;
    private int mCurrentWordCorrectCount;
    private int mCurrentWordIncorrectCount;
    private TextView mWord;
    private ArrayList<TextView> mAnswers;
    private TextView mCountdown;
    private CountDownTimer mCountDownTimer;
    private QuizQuestionActivity mContext;

    private static final int QUESTION_LOADER_ID = 1;

    public static final String TAG = "QuestionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_quiz_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialise textviews
        mWord = (TextView) findViewById(R.id.word);
        TextView answer1 = (TextView) findViewById(R.id.answer_1);
        TextView answer2 = (TextView) findViewById(R.id.answer_2);
        TextView answer3 = (TextView) findViewById(R.id.answer_3);
        TextView answer4 = (TextView) findViewById(R.id.answer_4);
        mAnswers = new ArrayList<>();
        mAnswers.add(answer1);
        mAnswers.add(answer2);
        mAnswers.add(answer3);
        mAnswers.add(answer4);

        mCountdown = (TextView) findViewById(R.id.countdown);

        mQuiz = (Quiz) getIntent().getParcelableExtra(getString(R.string.quiz));
        getSupportLoaderManager().initLoader(QUESTION_LOADER_ID, null, this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //need to cancel timer here or continues running
        stopTimer();
    }

    public void showAnswer(){
        for(TextView view : mAnswers){
            if(view.getTag().equals(getString(R.string.correct))){
                view.setBackgroundColor(getResources().getColor(R.color.correctGreen));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.incorrectRed));
            }
        }
    }

    private void startTimer(){

        mCountdown.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        mCountDownTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                mCountdown.setText(millisUntilFinished / 1000 + "");
                if(millisUntilFinished < 3000){
                    mCountdown.setTextColor(getResources().getColor(R.color.incorrectRed));
                }
            }

            public void onFinish() {
                mCountdown.setText("-");
                stopTimer();
                Log.d(TAG, "Question timed out");
                incorrectAnswer();
                showAnswer();
                nextQuestion();
            }
        }.start();

    }

    private void stopTimer(){
        if(mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private void populateQuiz(Cursor cursor){

        //update
        int currentQuizQuestionId = cursor.getInt(ColumnProjections.COL_QQ_ID);
        mCurrentWordCorrectCount = cursor.getInt(ColumnProjections.COL_QQ_CORRECT_COUNT);
        mCurrentWordIncorrectCount = cursor.getInt(ColumnProjections.COL_QQ_INCORRECT_COUNT);

        Random randomGenerator = new Random();
        //randomly set position of correct word
        int correctAnswerPosition = randomGenerator.nextInt(4);


        mWord.setText(cursor.getString(ColumnProjections.COL_QQ_WORD));

        mAnswers.get(correctAnswerPosition).setText(cursor.getString(ColumnProjections.COL_QQ_DEFINITION));
        mAnswers.get(correctAnswerPosition).setTag(getString(R.string.correct));

        ArrayList<String> wrongDefinitions = new ArrayList<>();
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_1));
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_2));
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_3));

        //loop through answers setting up textview
        for(int i=0; i<mAnswers.size(); i++){
            //reset background colors
            mAnswers.get(i).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mAnswers.get(i).setOnClickListener(this);

            if(i!= correctAnswerPosition) {
                mAnswers.get(i).setText(wrongDefinitions.get(0));
                mAnswers.get(i).setTag(getString(R.string.incorrect));
                wrongDefinitions.remove(0);
            }
        }
        //start countdown timer
        startTimer();
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader loader =  new CursorLoader(
                this,   // Parent activity context
                WideWordsProvider.QuizQuestion.withId(mQuiz.getCurrentQuizQuestionId()),        // Table to query
                ColumnProjections.QUIZ_QUESTION_COLUMNS,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst()){
            populateQuiz(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onClick(View view) {

        //stopTimer
        stopTimer();

        //first, disable subsequent button presses by removing click listeners
        for(TextView answer : mAnswers){
            answer.setOnClickListener(null);
        }

        if(view.getTag().equals(getString(R.string.correct))){
            correctAnswer();
        } else {
            incorrectAnswer();
        }

        showAnswer();
        nextQuestion();
    }

    private void correctAnswer() {
        Log.d(TAG, "Correct Answer");
        mQuiz.getQuizQuestionResults().set(mQuiz.getQuestionPosition(), 1);
        mCurrentWordCorrectCount++;
        mQuiz.getQuizQuestionCorrectCount().set(mQuiz.getQuestionPosition(), mCurrentWordCorrectCount);
        mQuiz.getQuizQuestionIncorrectCount().set(mQuiz.getQuestionPosition(), mCurrentWordIncorrectCount);
    }

    private void incorrectAnswer() {
        Log.d(TAG, "Incorrect Answer");
        mQuiz.getQuizQuestionResults().set(mQuiz.getQuestionPosition(), 0);
        mCurrentWordIncorrectCount++;
        mQuiz.getQuizQuestionCorrectCount().set(mQuiz.getQuestionPosition(), mCurrentWordCorrectCount);
        mQuiz.getQuizQuestionIncorrectCount().set(mQuiz.getQuestionPosition(), mCurrentWordIncorrectCount);
    }

    private void nextQuestion() {

        final CountDownTimer nextQuestionCountdown = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long l) {
                //no action required
            }

            public void onFinish() {
                if(mQuiz.nextQuestion() == false){
                    stopTimer();
                    Intent intent = new Intent(mContext, ResultsActivity.class);
                    intent.putExtra(getString(R.string.quiz), mQuiz);
                    startActivity(intent);
                    finish();
                } else {
                    getSupportLoaderManager().restartLoader(QUESTION_LOADER_ID, null, mContext);
                }
            }
        }.start();

    }
}
