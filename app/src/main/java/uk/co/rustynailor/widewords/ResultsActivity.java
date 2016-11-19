package uk.co.rustynailor.widewords;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import uk.co.rustynailor.widewords.data.ColumnProjections;
import uk.co.rustynailor.widewords.data.QuizQuestionColumns;
import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;
import uk.co.rustynailor.widewords.enums.QuizQuestionResult;
import uk.co.rustynailor.widewords.models.Quiz;

import static uk.co.rustynailor.widewords.data.ColumnProjections.WORD_COLUMNS;

public class ResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mResultsView;
    private TextView mMasteredResultsView;
    private TextView mMasteredRemainingView;
    private TextView mFinishButton;
    private Quiz mQuiz;

    private static final int QUESTION_RESULTS_LOADER_ID = 1;
    private static final int MASTERED_WORDS_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mResultsView = (TextView) findViewById(R.id.results_container);
        mMasteredResultsView = (TextView) findViewById(R.id.mastered_container);
        mMasteredRemainingView = (TextView) findViewById(R.id.mastered_remaining_container);
        mFinishButton = (TextView) findViewById(R.id.finish_button);

        // exit activity when finish is pressed
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mQuiz = getIntent().getParcelableExtra(getString(R.string.quiz));

        //save results to db
        for (int i = 0; i<mQuiz.getQuizQuestions().size(); i++){
            saveAnswer(i);
        }

        getSupportLoaderManager().initLoader(QUESTION_RESULTS_LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(MASTERED_WORDS_LOADER_ID, null, this);
    }


    public void saveAnswer(int position) {
        if (mQuiz.getQuestionPosition() > 0) {

            //get word id
            Cursor c = getContentResolver().query(WideWordsProvider.QuizQuestion.withId(mQuiz.getQuizQuestions().get(position)), ColumnProjections.QUIZ_QUESTION_COLUMNS, null, null, null);
            int wordId = 0;
            if(c.moveToFirst()){
                wordId = c.getInt(ColumnProjections.COL_QQ_WORD_ID);
            }
            c.close();


            if(wordId > 0) {
                //update correct count for word
                ContentValues updateWord = new ContentValues();
                updateWord.put(WordColumns.CORRECT_COUNT, mQuiz.getQuizQuestionCorrectCount().get(position));
                updateWord.put(WordColumns.INCORRECT_COUNT, mQuiz.getQuizQuestionIncorrectCount().get(position));
                getContentResolver().update(WideWordsProvider.Words.withId(wordId), updateWord, null, null);

                if(mQuiz.getQuizQuestionResults().get(position) == 1)
                {
                    //update quiz question - correct
                    ContentValues updateQuizQuestion = new ContentValues();
                    updateQuizQuestion.put(QuizQuestionColumns.QUIZ_QUESTION_RESULT, QuizQuestionResult.CORRECT.toString());
                    getContentResolver().update(WideWordsProvider.QuizQuestion.withId(mQuiz.getQuizQuestions().get(position)), updateQuizQuestion, null, null);

                } else {
                    //update quiz question - incorrect
                    ContentValues updateQuizQuestion = new ContentValues();
                    updateQuizQuestion.put(QuizQuestionColumns.QUIZ_QUESTION_RESULT, QuizQuestionResult.INCORRECT.toString());
                    getContentResolver().update(WideWordsProvider.QuizQuestion.withId(mQuiz.getQuizQuestions().get(position)), updateQuizQuestion, null, null);

                }
            }

        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case QUESTION_RESULTS_LOADER_ID:
                // Returns a new CursorLoader with quiz questions
                return new CursorLoader(
                        this,   // Parent activity context
                        WideWordsProvider.QuizQuestion.fromQuiz(mQuiz.getId()),        // Table to query
                        ColumnProjections.QUIZ_QUESTION_COLUMNS,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            case MASTERED_WORDS_LOADER_ID:
                // Returns a new CursorLoader for unmastered words
                String query =  WideWordsDatabase.WORDS + "." + WordColumns.CORRECT_COUNT + " <  ?";
                String[] params =  {"3"};

                return new CursorLoader(
                        this,   // Parent activity context
                        WideWordsProvider.Words.CONTENT_URI,        // Table to query
                        ColumnProjections.WORD_COLUMNS,     // Projection to return
                        query,            // selection clause
                        params,            //selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case QUESTION_RESULTS_LOADER_ID:
                int correctCount = 0;
                int masteredCount= 0;

                while (data.moveToNext()){

                    if(data.getInt(ColumnProjections.COL_QQ_CORRECT_COUNT) >= 3){
                        masteredCount++;
                    }

                    QuizQuestionResult quizQuestionResult = QuizQuestionResult.valueOf(data.getString(ColumnProjections.COL_QQ_RESULT));

                    if(quizQuestionResult.equals(QuizQuestionResult.CORRECT)){
                        correctCount++;
                    }
                }

                mResultsView.setText(correctCount + "/" + mQuiz.getQuizQuestionResults().size() + getString(R.string.correct_with_space));
                mMasteredResultsView.setText(masteredCount + getString(R.string.words_mastered));
                break;
            case MASTERED_WORDS_LOADER_ID:
                mMasteredRemainingView.setText(getString(R.string.you_have) + data.getCount() + getString(R.string.words_left_to_master));
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //cursor not retained sd no reset neccessary
    }
}
