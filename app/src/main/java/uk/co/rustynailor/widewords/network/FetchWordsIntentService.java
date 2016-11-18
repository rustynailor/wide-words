package uk.co.rustynailor.widewords.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import uk.co.rustynailor.widewords.R;
import uk.co.rustynailor.widewords.data.WideWordsProvider;
import uk.co.rustynailor.widewords.data.WordColumns;

/**
 * Created by russellhicks on 17/11/2016.
 */

public class FetchWordsIntentService extends IntentService {

    public static final String WORD_UPDATE_URL = "http://www.scapegrace.co.uk/wordupdate.json";
    public static final String SERVICE_TAG = "FetchWordsIntentService";

    private int mUpdateCount = 0;
    private int mInsertCount = 0;

    private boolean mUpdateFailed = false;

    public FetchWordsIntentService() {
        super(SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //do sync here
        //get json file
        String wordString = getStringFromService();

        if(wordString == null){
            mUpdateFailed = true;
        } else {
            //update database
            try {
                getWordDataFromJson(wordString);
            } catch (JSONException e) {
                mUpdateFailed = true;
                Log.e(SERVICE_TAG, "Error parsing JSON: " + wordString);
            }
        }
    }

    private String getStringFromService(){
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String wordJsonStr = null;
        try {

            //set URL for update service
            URL url = new URL(WORD_UPDATE_URL);

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            wordJsonStr = buffer.toString();
        } catch (IOException e) {
            mUpdateFailed = true;
            Log.e(SERVICE_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(SERVICE_TAG, "Error closing stream", e);
                }
            }
        }

        return wordJsonStr;
    }

    /**
     * Take the String representing the words in JSON Format and
     * insert any new data into the database
     */
    private String[] getWordDataFromJson(String wordsStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String JSON_WORDS_ARRAY = "words";
        final String JSON_WORD = "word";
        final String JSON_DEFINITION= "definition";


        try {
            JSONObject wordsJson = new JSONObject(wordsStr);
            JSONArray wordsArray = wordsJson.getJSONArray(JSON_WORDS_ARRAY);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>();


            for(int i=0;i<wordsArray.length();i++){

                JSONObject wordObject = wordsArray.getJSONObject(i);
                String word = wordObject.getString(JSON_WORD);
                String definition = wordObject.getString(JSON_DEFINITION);

                ContentValues contentValues = new ContentValues();
                contentValues.put(WordColumns.WORD, word);
                contentValues.put(WordColumns.DEFINITION, definition);

                //check if word exists in database - if not, insert
                int updateCount = getApplicationContext().getContentResolver().update(WideWordsProvider.Words.withWord(word), contentValues, null, null);
                mUpdateCount = mUpdateCount + updateCount;
                //if no update, do insert
                if(updateCount == 0){
                    getApplicationContext().getContentResolver().insert(WideWordsProvider.Words.CONTENT_URI, contentValues);
                    mInsertCount++;
                }

            }


        } catch (JSONException e) {
            Log.e(SERVICE_TAG, e.getMessage(), e);
            mUpdateFailed = true;
        }
        return null;

    }

    @Override
    public void onDestroy() {
        if(mUpdateFailed){
            Toast.makeText(this, "Update from server failed. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.update_message, mUpdateCount, mInsertCount), Toast.LENGTH_SHORT).show();

        }
        super.onDestroy();
    }


}
