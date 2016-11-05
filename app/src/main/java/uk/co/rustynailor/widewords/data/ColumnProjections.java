package uk.co.rustynailor.widewords.data;

/**
 * Created by russellhicks on 05/11/2016.
 */

public class ColumnProjections {


    //basic word projection
    public static final String[] WORD_COLUMNS = {
            WideWordsDatabase.WORDS + "." + WordColumns._ID,
            WideWordsDatabase.WORDS + "." + WordColumns.DEFINITION,
            WideWordsDatabase.WORDS + "." + WordColumns.WORD
    };


    // These indices are tied to WORD_COLUMNS.  If WORD_COLUMNS change, these
    // must change.
    public static final int COL_WORD_ID = 0;
    public static final int COL_WORD_DEFINITION = 1;
    public static final int COL_WORD_WORD = 2;


    //quiz question with joins to words and definitions

    //table aliases for joins
    public static final String CORRECT_WORD_TABLE_ALIAS = "CORRECT_WORD_TABLE_ALIAS";
    public static final String INCORRECT_WORD1_TABLE_ALIAS = "INCORRECT_WORD1_TABLE_ALIAS";
    public static final String INCORRECT_WORD2_TABLE_ALIAS = "INCORRECT_WORD2_TABLE_ALIAS";
    public static final String INCORRECT_WORD3_TABLE_ALIAS = "INCORRECT_WORD3_TABLE_ALIAS";


    public static final String[] QUIZ_QUESTION_COLUMNS = {
            WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns._ID,
            CORRECT_WORD_TABLE_ALIAS + "." + WordColumns._ID,
            CORRECT_WORD_TABLE_ALIAS + "." + WordColumns.WORD,
            CORRECT_WORD_TABLE_ALIAS  + "." + WordColumns.DEFINITION,
            CORRECT_WORD_TABLE_ALIAS  + "." + WordColumns.CORRECT_COUNT,
            CORRECT_WORD_TABLE_ALIAS  + "." + WordColumns.INCORRECT_COUNT,
            INCORRECT_WORD1_TABLE_ALIAS + "." + WordColumns.DEFINITION,
            INCORRECT_WORD2_TABLE_ALIAS + "." + WordColumns.DEFINITION,
            INCORRECT_WORD3_TABLE_ALIAS + "." + WordColumns.DEFINITION

    };


    // These indices are tied to QUIZ_QUESTION_COLUMNS.  If WORD_COLUMNS change, these
    // must change.
    public static final int COL_QQ_ID = 0;
    public static final int COL_QQ_WORD_ID = 1;
    public static final int COL_QQ_WORD = 2;
    public static final int COL_QQ_DEFINITION = 3;
    public static final int COL_QQ_CORRECT_COUNT = 4;
    public static final int COL_QQ_INCORRECT_COUNT = 5;
    public static final int COL_QQ_WRONG_1 = 6;
    public static final int COL_QQ_WRONG_2 = 7;
    public static final int COL_QQ_WRONG_3 = 8;
}
