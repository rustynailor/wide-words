package uk.co.rustynailor.widewords.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import uk.co.rustynailor.widewords.enums.QuizQuestionResult;

/**
 * Created by russellhicks on 25/10/2016.
 */

public interface QuizQuestionColumns {
    //primary key auto increment integer
    // _id = mId
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";
    // quiz_id = mQuizId
    @DataType(DataType.Type.INTEGER) @References(table = WideWordsDatabase.QUIZ, column = QuizColumns._ID) String QUIZ_ID = "quiz_id";
    // word_id = mWordId
    @DataType(DataType.Type.INTEGER) @References(table = WideWordsDatabase.WORDS, column = WordColumns._ID) String WORD_ID = "word_id";
    // position = mPosition
    @DataType(DataType.Type.INTEGER) String POSITION = "POSITION";
    // wrong_definition_1_id = mWrongDefinition1Id
    @DataType(DataType.Type.INTEGER) @References(table = WideWordsDatabase.WORDS, column = WordColumns._ID) String WRONG_DEFINITION_1_ID = "wrong_definition_1_id";
    // wrong_definition_2_id = mWrongDefinition2Id
    @DataType(DataType.Type.INTEGER) @References(table = WideWordsDatabase.WORDS, column = WordColumns._ID) String WRONG_DEFINITION_2_ID = "wrong_definition_2_id";
    // wrong_definition_2_id = mWrongDefinition3Id
    @DataType(DataType.Type.INTEGER) @References(table = WideWordsDatabase.WORDS, column = WordColumns._ID) String WRONG_DEFINITION_3_ID = "wrong_definition_3_id";
    // answer_given = mAnswerGiven;
    @DataType(DataType.Type.INTEGER) @References(table = WideWordsDatabase.WORDS, column = WordColumns._ID) String ANSWER_GIVEN = "answer_given";
    // quiz_question_result = mQuizQuestionResult
    // enum field stored as TEXT
    @DataType(DataType.Type.TEXT) String QUIZ_QUESTION_RESULT = "quiz_question_result";



}
