package uk.co.rustynailor.widewords.network;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by russellhicks on 17/11/2016.
 */

public class FetchWordsIntentService extends IntentService {

    public static final String WORD_UPDATE_URL = "http://www.scapegrace.co.uk/wordupdate.json";
    public static final String SERVICE_TAG = "FetchWordsIntentService";

    public FetchWordsIntentService() {
        super(SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //do sync here

        //get json file

        //update database

        //alert user somehow - toast?
    }
}
