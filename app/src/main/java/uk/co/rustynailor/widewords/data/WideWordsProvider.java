package uk.co.rustynailor.widewords.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

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
        String NEW_WORDS = "new_words";
        String QUIZZES = "quizzes";
        String QUIZ_QUESTIONS = "quiz_questions";
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


    }
}