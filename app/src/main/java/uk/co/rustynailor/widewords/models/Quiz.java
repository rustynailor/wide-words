package uk.co.rustynailor.widewords.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import uk.co.rustynailor.widewords.enums.QuizQuestionResult;
import uk.co.rustynailor.widewords.enums.QuizStatus;

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
    //quiz answers (1 = correct, 0 = incorrect)
    private ArrayList<Integer> mQuizQuestionResults;
    //quiz questions correct count
    private ArrayList<Integer> mQuizQuestionCorrectCount;
    //quiz questions Incorrect count
    private ArrayList<Integer> mQuizQuestionIncorrectCount;
    //retain countdown position
    private long mRemaining;
    //quiz status - used to track position in quiz
    private QuizStatus mQuizStatus;



    //empty constructor
    public Quiz() {
        mQuizQuestions = new ArrayList<>();
        mQuizQuestionResults = new ArrayList<>();
        mQuizQuestionCorrectCount = new ArrayList<>();
        mQuizQuestionIncorrectCount = new ArrayList<>();
        //default value for Quiz status
        mQuizStatus = QuizStatus.IN_PROGRESS;
    }


    protected Quiz(Parcel in) {
        mId = in.readInt();
        mStarted = in.readLong();
        mQuestionPosition = in.readInt();
        mRemaining = in.readLong();
        mQuizQuestions = new ArrayList<Integer>();
        in.readList(mQuizQuestions, Integer.class.getClassLoader());
        mQuizQuestionResults = new ArrayList<Integer>();
        in.readList(mQuizQuestionResults, Integer.class.getClassLoader());
        mQuizQuestionCorrectCount = new ArrayList<Integer>();
        in.readList(mQuizQuestionCorrectCount, Integer.class.getClassLoader());
        mQuizQuestionIncorrectCount = new ArrayList<Integer>();
        in.readList(mQuizQuestionIncorrectCount, Integer.class.getClassLoader());
        try {
            mQuizStatus = QuizStatus.valueOf(in.readString());
        } catch (IllegalArgumentException x) {
            mQuizStatus = null;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeLong(mStarted);
        dest.writeInt(mQuestionPosition);
        dest.writeLong(mRemaining);
        dest.writeList(mQuizQuestions);
        dest.writeList(mQuizQuestionResults);
        dest.writeList(mQuizQuestionCorrectCount);
        dest.writeList(mQuizQuestionIncorrectCount);
        dest.writeString((mQuizStatus == null) ? "" : mQuizStatus.name());
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

    public ArrayList<Integer> getQuizQuestionResults() {
        return mQuizQuestionResults;
    }

    public void setQuizQuestionResults(ArrayList<Integer> quizQuestionResults) {
        mQuizQuestionResults = quizQuestionResults;
    }

    public ArrayList<Integer> getQuizQuestionCorrectCount() {
        return mQuizQuestionCorrectCount;
    }

    public void setQuizQuestionCorrectCount(ArrayList<Integer> quizQuestionCorrectCount) {
        mQuizQuestionCorrectCount = quizQuestionCorrectCount;
    }

    public ArrayList<Integer> getQuizQuestionIncorrectCount() {
        return mQuizQuestionIncorrectCount;
    }

    public void setQuizQuestionIncorrectCount(ArrayList<Integer> quizQuestionIncorrectCount) {
        mQuizQuestionIncorrectCount = quizQuestionIncorrectCount;
    }

    public boolean nextQuestion(){
        boolean success;
        if((mQuestionPosition + 1) >= mQuizQuestions.size()){
            success =  false;
        } else {
            mQuestionPosition++;
            mQuizStatus = QuizStatus.IN_PROGRESS;
            success =  true;
        }
        return success;
    }

    public long getRemaining() {
        return mRemaining;
    }

    public void setRemaining(long remaining) {
        mRemaining = remaining;
    }

    public QuizStatus getQuizStatus() {
        return mQuizStatus;
    }

    public void setQuizStatus(QuizStatus quizStatus) {
        mQuizStatus = quizStatus;
    }
}
