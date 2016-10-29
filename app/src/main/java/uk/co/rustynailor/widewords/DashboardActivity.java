package uk.co.rustynailor.widewords;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.rustynailor.widewords.data.InitialData;
import uk.co.rustynailor.widewords.data.WideWordsDatabase;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;

public class DashboardActivity extends AppCompatActivity {

    public static final String[] WORD_COLUMNS = {
            WideWordsDatabase.WORDS + "." + WordColumns._ID,
            WideWordsDatabase.WORDS + "." + WordColumns.WORD,
            WideWordsDatabase.WORDS + "." + WordColumns.DEFINITION

    };


    // These indices are tied to ASSIGNMENT_COLUMNS.  If ASSIGNMENT_COLUMNS changes, these
    // must change.
    public static final int COL_WORD_ID = 0;
    public static final int COL_WORD_WORD = 1;
    public static final int COL_WORD_DEFINITION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Cursor c = getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                null, null, null, null);
        Log.i("Dashboard", "cursor count: " + c.getCount());
        if (c == null || c.getCount() == 0){
            insertData();
        }
        c.close();

        Cursor cursor = getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                WORD_COLUMNS, null, null, null);

        TextView hello = (TextView) findViewById(R.id.hello);

        if(cursor.getColumnCount() > 0){
            while(cursor.moveToNext()){
                hello.append(" " + cursor.getString(COL_WORD_WORD));
            }
        }

        cursor.close();
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
