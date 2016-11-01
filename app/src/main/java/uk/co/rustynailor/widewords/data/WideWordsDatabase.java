package uk.co.rustynailor.widewords.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by russellhicks on 25/10/2016.
 */

@Database(version = WideWordsDatabase.VERSION)

public final class WideWordsDatabase {

    private WideWordsDatabase() {

    }

    public static final int VERSION = 1;

    @Table(WordColumns.class)
    public static final String WORDS = "words";

    @Table(QuizColumns.class)
    public static final String QUIZ = "quiz";

    @Table(QuizQuestionColumns.class)
    public static final String QUIZ_QUESTION = "quiz_questions";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {

        String[] initialWords =  InitialData.initialWords;
        String[] initialDefs =  InitialData.initialdefinitions;

        for(int i=0; i< initialWords.length; i++){
           String sql = "INSERT INTO " + WORDS + " (" + WordColumns.WORD + "," + WordColumns.DEFINITION + ","
                   + WordColumns.CORRECT_COUNT + "," + WordColumns.INCORRECT_COUNT + ") " +
                    "VALUES (\"" + initialWords[i] + "\",\"" + initialDefs[i] + "\", 0, 0)";
            db.execSQL(sql);

        }

    }

}