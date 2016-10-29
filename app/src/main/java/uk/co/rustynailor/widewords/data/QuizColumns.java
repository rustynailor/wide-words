package uk.co.rustynailor.widewords.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by russellhicks on 25/10/2016.
 */

public interface QuizColumns {


    //primary key auto increment integer
    // _id = mId
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    // started = mStarted
    @DataType(DataType.Type.INTEGER) String STARTED = "started";

    // qurestion_position = mQuestionPosition
    @DataType(DataType.Type.INTEGER) String QUESTION_POSITION = "question_position";

}
