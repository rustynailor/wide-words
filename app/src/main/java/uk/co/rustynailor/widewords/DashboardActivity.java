package uk.co.rustynailor.widewords;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.rustynailor.widewords.data.InitialData;
import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;

import static uk.co.rustynailor.widewords.data.ColumnProjections.WORD_COLUMNS;

public class DashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cursor c = getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                null, null, null, null);
        Log.i("Dashboard", "cursor count: " + c.getCount());
        if (c == null || c.getCount() == 0){
            insertData();
        }
        c.close();

        Cursor cursor = getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                WORD_COLUMNS, null, null, null);

        cursor.close();

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
    }

    public void insertData(){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(InitialData.initialWords.length);

        for (int i = 0; i<InitialData.initialWords.length; i++){
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    WideWordsProvider.Words.CONTENT_URI);
            builder.withValue(WordColumns.WORD, InitialData.initialWords[i]);
            builder.withValue(WordColumns.DEFINITION, InitialData.initialdefinitions[i]);
            builder.withValue(WordColumns.INCORRECT_COUNT, 0);
            builder.withValue(WordColumns.CORRECT_COUNT, 0);
            batchOperations.add(builder.build());
        }

        try{
           getContentResolver().applyBatch(WideWordsProvider.AUTHORITY, batchOperations);
        } catch(RemoteException | OperationApplicationException e){
            Log.e("Dashboard", "Error applying batch insert", e);
        }

    }

}
