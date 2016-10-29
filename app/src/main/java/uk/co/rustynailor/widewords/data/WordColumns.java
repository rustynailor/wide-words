package uk.co.rustynailor.widewords.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by russellhicks on 25/10/2016.
 */

public interface WordColumns {

    //primary key auto increment integer
    // _id = mId
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    // word = mWord
    @DataType(DataType.Type.TEXT) String WORD = "word";

    // word = mWord
    @DataType(DataType.Type.TEXT) String DEFINITION = "definition";

    // correct_count = mCorrectCount
    @DataType(DataType.Type.INTEGER) String CORRECT_COUNT = "correct_count";

    // correct_count = mIncorrectCount
    @DataType(DataType.Type.INTEGER) String INCORRECT_COUNT = "incorrect_count";


}
