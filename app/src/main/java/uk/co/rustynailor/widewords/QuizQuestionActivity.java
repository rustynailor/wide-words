package uk.co.rustynailor.widewords;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.QuizQuestionColumns;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;
import uk.co.rustynailor.widewords.enums.QuizQuestionResult;
import uk.co.rustynailor.widewords.models.Quiz;

public class QuizQuestionActivity extends AppCompatActivity  implements View.OnClickListener, LoaderCallbacks<Cursor> {

    private Quiz mQuiz;
    private int mCurrentWordId;
    private int mCurrentWordCorrectCount;
    private int mCurrentWordIncorrectCount;
    private int mCurrentQuizQuestionId;
    private TextView mWord;
    private ArrayList<TextView> mAnswers;
    private int mCorrectAnswerPosition;
    private TextView mCountdown;
    private CountDownTimer mCountDownTimer;
    private QuizQuestionActivity mContext;

    private static final int QUESTION_LOADER_ID = 1;

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

        mQuiz = (Quiz) getIntent().getParcelableExtra("quiz");
        getSupportLoaderManager().initLoader(QUESTION_LOADER_ID, null, this);


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

        mCountDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                mCountdown.setText(millisUntilFinished / 1000 + "");
                if(millisUntilFinished < 3000){
                    mCountdown.setTextColor(getResources().getColor(R.color.incorrectRed));
                }
            }

            public void onFinish() {
                mCountdown.setText("-");
                Log.e("TAG", "Incorrect Answer");
                mQuiz.getQuizQuestionResults().set(mQuiz.getQuestionPosition(), 0);
                showAnswer();
                nextQuestion();
            }
        }.start();

    }

    private void stopTimer(){
        mCountDownTimer.cancel();
        mCountDownTimer = null;
    }

    private void populateQuiz(Cursor cursor){

        //first, update previous question in database
        saveAnswer(mQuiz.getQuestionPosition()-1);

        //update
        mCurrentQuizQuestionId = cursor.getInt(ColumnProjections.COL_QQ_ID);
        mCurrentWordId = cursor.getInt(ColumnProjections.COL_QQ_WORD_ID);
        mCurrentWordCorrectCount = cursor.getInt(ColumnProjections.COL_QQ_CORRECT_COUNT);
        mCurrentWordIncorrectCount = cursor.getInt(ColumnProjections.COL_QQ_INCORRECT_COUNT);

        Random randomGenerator = new Random();
        //randomly set position of correct word
        mCorrectAnswerPosition = randomGenerator.nextInt(4);


        mWord.setText(cursor.getString(ColumnProjections.COL_QQ_WORD));

        mAnswers.get(mCorrectAnswerPosition).setText(cursor.getString(ColumnProjections.COL_QQ_DEFINITION));
        mAnswers.get(mCorrectAnswerPosition).setTag(getString(R.string.correct));

        ArrayList<String> wrongDefinitions = new ArrayList<>();
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_1));
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_2));
        wrongDefinitions.add(cursor.getString(ColumnProjections.COL_QQ_WRONG_3));

        //loop through answers setting up textview
        for(int i=0; i<mAnswers.size(); i++){
            //reset background colors
            mAnswers.get(i).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mAnswers.get(i).setOnClickListener(this);

            if(i!=mCorrectAnswerPosition) {
                mAnswers.get(i).setText(wrongDefinitions.get(0));
                mAnswers.get(i).setTag(getString(R.string.incorrect));
                wrongDefinitions.remove(0);
            }
        }

        //start countdown timer
        startTimer();
    }

    public void saveAnswer(int position) {
        //only go ahead if this is the last question

        if (mQuiz.getQuestionPosition() > 0) {

            if(mQuiz.getQuizQuestionResults().get(position) == 1)
            {
                //update correct count for word
                ContentValues updateWord = new ContentValues();
                updateWord.put(WordColumns.CORRECT_COUNT, mCurrentWordCorrectCount+1);
                getContentResolver().update(WideWordsProvider.Words.withId(mCurrentWordId), updateWord, null, null);

                //update quiz question
                ContentValues updateQuizQuestion = new ContentValues();
                updateQuizQuestion.put(QuizQuestionColumns.QUIZ_QUESTION_RESULT, QuizQuestionResult.CORRECT.toString());
                getContentResolver().update(WideWordsProvider.QuizQuestion.withId(mCurrentQuizQuestionId), updateQuizQuestion, null, null);

            } else {
                //update incorrect count for word
                ContentValues updateWord = new ContentValues();
                updateWord.put(WordColumns.INCORRECT_COUNT, mCurrentWordIncorrectCount+1);
                getContentResolver().update(WideWordsProvider.Words.withId(mCurrentWordId), updateWord, null, null);

                //update quiz question
                ContentValues updateQuizQuestion = new ContentValues();
                updateQuizQuestion.put(QuizQuestionColumns.QUIZ_QUESTION_RESULT, QuizQuestionResult.INCORRECT.toString());
                getContentResolver().update(WideWordsProvider.QuizQuestion.withId(mCurrentQuizQuestionId), updateQuizQuestion, null, null);

            }
        }
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
            Log.e("TAG", "Correct Answer");
            mQuiz.getQuizQuestionResults().set(mQuiz.getQuestionPosition(), 1);
        } else {
            Log.e("TAG", "Incorrect Answer");
            mQuiz.getQuizQuestionResults().set(mQuiz.getQuestionPosition(), 0);
        }

        showAnswer();
        nextQuestion();
    }

    private void nextQuestion() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mQuiz.nextQuestion() == false){
                    saveAnswer(mQuiz.getQuestionPosition());
                    Intent intent = new Intent(mContext, ResultsActivity.class);
                    intent.putExtra("quiz", mQuiz);
                    startActivity(intent);
                    finish();
                } else {
                    getSupportLoaderManager().restartLoader(QUESTION_LOADER_ID, null, mContext);
                }
            }
        }, 3000);
    }
}
