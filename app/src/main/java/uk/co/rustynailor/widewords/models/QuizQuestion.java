package uk.co.rustynailor.widewords.models;

import android.os.Parcel;
import android.os.Parcelable;

import uk.co.rustynailor.widewords.enums.QuizQuestionResult;

/**
 * Created by russellhicks on 25/10/2016.
 */

public class QuizQuestion implements Parcelable{

    private int mId;
    private int mQuizId;
    private int mWordId;
    private int mPosition;
    private int mWrongDefinition1Id;
    private int mWrongDefinition2Id;
    private int mWrongDefinition3Id;
    private int mAnswerGiven;
    private QuizQuestionResult mQuizQuestionResult;

    public QuizQuestion() {
        //default QuizQuestionStatus is new (in queue)
        mQuizQuestionResult = QuizQuestionResult.QUEUE;
    }

    protected QuizQuestion(Parcel in) {
        mId = in.readInt();
        mQuizId = in.readInt();
        mWordId = in.readInt();
        mPosition = in.readInt();
        mWrongDefinition1Id = in.readInt();
        mWrongDefinition2Id = in.readInt();
        mWrongDefinition3Id = in.readInt();
        mAnswerGiven = in.readInt();
        try {
            mQuizQuestionResult = QuizQuestionResult.valueOf(in.readString());
        } catch (IllegalArgumentException x) {
            mQuizQuestionResult = null;
        }
    }

    public static final Creator<QuizQuestion> CREATOR = new Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getQuizId() {
        return mQuizId;
    }

    public void setQuizId(int quizId) {
        mQuizId = quizId;
    }

    public int getWordId() {
        return mWordId;
    }

    public void setWordId(int wordId) {
        mWordId = wordId;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getWrongDefinition1Id() {
        return mWrongDefinition1Id;
    }

    public void setWrongDefinition1Id(int wrongDefinition1Id) {
        mWrongDefinition1Id = wrongDefinition1Id;
    }

    public int getWrongDefinition2Id() {
        return mWrongDefinition2Id;
    }

    public void setWrongDefinition2Id(int wrongDefinition2Id) {
        mWrongDefinition2Id = wrongDefinition2Id;
    }

    public int getWrongDefinition3Id() {
        return mWrongDefinition3Id;
    }

    public void setWrongDefinition3Id(int wrongDefinition3Id) {
        mWrongDefinition3Id = wrongDefinition3Id;
    }

    public QuizQuestionResult getQuizQuestionResult() {
        return mQuizQuestionResult;
    }

    public void setQuizQuestionResult(QuizQuestionResult quizQuestionResult) {
        mQuizQuestionResult = quizQuestionResult;
    }

    public int getAnswerGiven() {
        return mAnswerGiven;
    }

    public void setAnswerGiven(int answerGiven) {
        mAnswerGiven = answerGiven;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeInt(mQuizId);
        parcel.writeInt(mWordId);
        parcel.writeInt(mPosition);
        parcel.writeInt(mWrongDefinition1Id);
        parcel.writeInt(mWrongDefinition2Id);
        parcel.writeInt(mWrongDefinition3Id);
        parcel.writeInt(mAnswerGiven);
        parcel.writeString((mQuizQuestionResult == null) ? "" : mQuizQuestionResult.name());
    }
}
