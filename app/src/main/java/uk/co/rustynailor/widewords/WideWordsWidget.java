package uk.co.rustynailor.widewords;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

import uk.co.rustynailor.widewords.data.WideWordsProvider;

import static uk.co.rustynailor.widewords.data.ColumnProjections.COL_WORD_DEFINITION;
import static uk.co.rustynailor.widewords.data.ColumnProjections.COL_WORD_WORD;
import static uk.co.rustynailor.widewords.data.ColumnProjections.WORD_COLUMNS;

/**
 * Implementation of App Widget functionality.
 * Button click code based on this post from SO:
 * http://stackoverflow.com/questions/14798073/button-click-event-for-android-widget
 */
public class WideWordsWidget extends AppWidgetProvider {

    public static String WIDGET_BUTTON = "uk.co.rustynailor.widewords.UPDATE_WORD_BUTTON";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.e("Log", "update app widget called");

        //this is the data we present
        String word;
        String definition;

        //get random word and definition
        Cursor c = context.getContentResolver().query(WideWordsProvider.Words.CONTENT_URI,
                WORD_COLUMNS, null, null, null);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wide_words_widget);

        //if we have some words, select a random one
        if(c.getCount() > 0){

            Random randomGenerator = new Random();
            int randomWordPosition = randomGenerator.nextInt(c.getCount()-1);

            c.moveToPosition(randomWordPosition);

            word = c.getString(COL_WORD_WORD);
            definition = c.getString(COL_WORD_DEFINITION);
            views.setTextViewText(R.id.appwidget_word, word);
            views.setTextViewText(R.id.appwidget_definition, definition);

        }
        c.close();

        //add intent to launch app on click
        Intent intent = new Intent(context, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_word, pendingIntent);
        views.setOnClickPendingIntent(R.id.appwidget_definition,pendingIntent);

        //add an intent to handle button clicks
        Intent intentSync = new Intent(context, WideWordsWidget.class);
        intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingSync = PendingIntent.getBroadcast(context, 0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT); //You need to specify a proper flag for the intent. Or else the intent will become deleted.
        views.setOnClickPendingIntent(R.id.refresh_widget_button,pendingSync);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //update widgets
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

