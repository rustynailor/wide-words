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
    private ArrayList<Integer> mQuizQuestions;

    //empty constructor
    public Quiz() {

        mQuizQuestions = new ArrayList<>();
    }


    protected Quiz(Parcel in) {
        mId = in.readInt();
        mStarted = in.readLong();
        mQuestionPosition = in.readInt();
        mQuizQuestions = new ArrayList<Integer>();
        in.readList(mQuizQuestions, Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeLong(mStarted);
        dest.writeInt(mQuestionPosition);
        dest.writeList(mQuizQuestions);
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

    public ArrayList<Integer> getQuizQuestions() {
        return mQuizQuestions;
    }

    public void setQuizQuestions(ArrayList<Integer> quizQuestions) {
        mQuizQuestions = quizQuestions;
    }

    public void addQuizQuestionId(Integer quizQuestionId) {
        mQuizQuestions.add(quizQuestionId);
    }

    public int getCurrentQuizQuestionId()
    {
        return mQuizQuestions.get(mQuestionPosition);
    }

    public boolean nextQuestion(){
        boolean success;
        if((mQuestionPosition + 1) >= mQuizQuestions.size()){
            success =  false;
        } else {
            mQuestionPosition++;
            success =  true;
        }
        return success;
    }
}
