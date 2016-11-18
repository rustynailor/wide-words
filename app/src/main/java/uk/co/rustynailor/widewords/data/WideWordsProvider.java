package uk.co.rustynailor.widewords.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static uk.co.rustynailor.widewords.data.ColumnProjections.CORRECT_WORD_TABLE_ALIAS;
import static uk.co.rustynailor.widewords.data.ColumnProjections.INCORRECT_WORD1_TABLE_ALIAS;
import static uk.co.rustynailor.widewords.data.ColumnProjections.INCORRECT_WORD2_TABLE_ALIAS;
import static uk.co.rustynailor.widewords.data.ColumnProjections.INCORRECT_WORD3_TABLE_ALIAS;

/**
 * Created by russellhicks on 25/10/2016.
 */

@ContentProvider(authority = WideWordsProvider.AUTHORITY,
        database = WideWordsDatabase.class)
public final class WideWordsProvider {

    private WideWordsProvider() {
    }

    public static final String AUTHORITY = "uk.co.rustynailor.widewords.data.WideWordsProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String WORDS = "words";
        String WITH_WORD = "with_word";
        String QUIZZES = "quizzes";
        String QUIZ_QUESTIONS = "quiz_questions";
        String FROM_QUIZ = "from_quiz";
        String FROM_QUIZ_QUESTION = "from_quiz_question";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = WideWordsDatabase.WORDS) public static class Words {

        @ContentUri(
                path = Path.WORDS,
                type = "vnd.android.cursor.dir/word")
        public static final Uri CONTENT_URI = buildUri(Path.WORDS);

        @InexactContentUri(
                path = Path.WORDS + "/#",
                name = "_ID",
                type = "vnd.android.cursor.item/word",
                whereColumn = WordColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.WORDS, String.valueOf(id));
        }

        @InexactContentUri(
                path = Path.WITH_WORD + "/*",
                name = "WITH_WORD",
                type = "vnd.android.cursor.item/word",
                whereColumn = WordColumns.WORD,
                pathSegment = 1)
        public static Uri withWord(String word) {
            return buildUri(Path.WITH_WORD, word);
        }

    }

    @TableEndpoint(table = WideWordsDatabase.QUIZ) public static class Quiz {

        @ContentUri(
                path = Path.QUIZZES,
                type = "vnd.android.cursor.dir/quiz")
        public static final Uri CONTENT_URI = buildUri(Path.QUIZZES);

        @InexactContentUri(
                path = Path.QUIZZES + "/#",
                name = "_ID",
                type = "vnd.android.cursor.item/quiz",
                whereColumn = WordColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.QUIZZES, String.valueOf(id));
        }


    }

    @TableEndpoint(table = WideWordsDatabase.QUIZ_QUESTION) public static class QuizQuestion {

        @ContentUri(
                path = Path.QUIZ_QUESTIONS,
                type = "vnd.android.cursor.dir/quiz_question")
        public static final Uri CONTENT_URI = buildUri(Path.QUIZ_QUESTIONS);

        @InexactContentUri(
                path = Path.QUIZ_QUESTIONS + "/" + Path.FROM_QUIZ + "/#",
                name = "QUIZ_QUESTIONS_FROM_QUIZ",
                type = "vnd.android.cursor.dir/quiz_question",
                whereColumn = WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.QUIZ_ID,
                pathSegment = 2,
                join =  " JOIN " + WideWordsDatabase.WORDS + " AS " + CORRECT_WORD_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WORD_ID + " = " + CORRECT_WORD_TABLE_ALIAS + "." + WordColumns._ID
                        + " JOIN " + WideWordsDatabase.WORDS + " AS " + INCORRECT_WORD1_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WRONG_DEFINITION_1_ID + " = " + INCORRECT_WORD1_TABLE_ALIAS + "." + WordColumns._ID
                        + " JOIN " + WideWordsDatabase.WORDS + " AS " + INCORRECT_WORD2_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WRONG_DEFINITION_2_ID + " = " + INCORRECT_WORD2_TABLE_ALIAS + "." + WordColumns._ID
                        + " JOIN " + WideWordsDatabase.WORDS + " AS " + INCORRECT_WORD3_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WRONG_DEFINITION_3_ID + " = " + INCORRECT_WORD3_TABLE_ALIAS + "." + WordColumns._ID


        )
        public static Uri fromQuiz(long id) {
            return buildUri(Path.QUIZ_QUESTIONS, Path.FROM_QUIZ, String.valueOf(id));
        }

        @InexactContentUri(
                path = Path.QUIZ_QUESTIONS + "/#",
                name = "_ID",
                type = "vnd.android.cursor.item/quiz_question",
                whereColumn = WideWordsDatabase.QUIZ_QUESTION + "." + QuizColumns._ID,
                pathSegment = 1,
                join =  " JOIN " + WideWordsDatabase.WORDS + " AS " + CORRECT_WORD_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WORD_ID + " = " + CORRECT_WORD_TABLE_ALIAS + "." + WordColumns._ID
                        + " JOIN " + WideWordsDatabase.WORDS + " AS " + INCORRECT_WORD1_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WRONG_DEFINITION_1_ID + " = " + INCORRECT_WORD1_TABLE_ALIAS + "." + WordColumns._ID
                        + " JOIN " + WideWordsDatabase.WORDS + " AS " + INCORRECT_WORD2_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WRONG_DEFINITION_2_ID + " = " + INCORRECT_WORD2_TABLE_ALIAS + "." + WordColumns._ID
                        + " JOIN " + WideWordsDatabase.WORDS + " AS " + INCORRECT_WORD3_TABLE_ALIAS + " ON " + WideWordsDatabase.QUIZ_QUESTION + "." + QuizQuestionColumns.WRONG_DEFINITION_3_ID + " = " + INCORRECT_WORD3_TABLE_ALIAS + "." + WordColumns._ID


        )
        public static Uri withId(long id) {
            return buildUri(Path.QUIZ_QUESTIONS, String.valueOf(id));
        }


    }
}