package uk.co.rustynailor.widewords.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by russellhicks on 25/10/2016.
 */

public class Quiz implements Parcelable {

    //id
    private int mId;
    //timestamp as started
    private long mStarted;
    //position in quiz
    private int mQuestionPosition;
    //quiz questions
    private ArrayList<QuizQuestion> mQuizQuestions;

    //empty constructor
    public Quiz() {
        mQuizQuestions = new ArrayList<QuizQuestion>();
    }

    protected Quiz(Parcel in) {
        mId = in.readInt();
        mStarted = in.readLong();
        mQuestionPosition = in.readInt();
        mQuizQuestions = in.createTypedArrayList(QuizQuestion.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeLong(mStarted);
        dest.writeInt(mQuestionPosition);
        dest.writeTypedList(mQuizQuestions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public long getStarted() {
        return mStarted;
    }

    public void setStarted(long started) {
        mStarted = started;
    }

    public int getQuestionPosition() {
        return mQuestionPosition;
    }

    public void setQuestionPosition(int questionPosition) {
        mQuestionPosition = questionPosition;
    }

    public ArrayList<QuizQuestion> getQuizQuestions() {
        return mQuizQuestions;
    }

    public void setQuizQuestions(ArrayList<QuizQuestion> quizQuestions) {
        mQuizQuestions = quizQuestions;
    }
}
