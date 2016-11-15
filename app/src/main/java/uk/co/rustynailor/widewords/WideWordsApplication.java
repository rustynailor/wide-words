package uk.co.rustynailor.widewords;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by russellhicks on 15/11/2016.
 */

public class WideWordsApplication extends Application {
    public static FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
