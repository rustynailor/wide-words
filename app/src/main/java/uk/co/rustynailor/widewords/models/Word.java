package uk.co.rustynailor.widewords.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by russellhicks on 25/10/2016.
 */

public class Word implements Parcelable{

    private int mId;
    private String mWord;
    private String mDefinition;
    private int mCorrectCount;
    private int mIncorrectCount;

    //empty constructor
    public Word() {
    }

    //getters and setters

    protected Word(Parcel in) {
        mId = in.readInt();
        mWord = in.readString();
        mDefinition = in.readString();
        mCorrectCount = in.readInt();
        mIncorrectCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mWord);
        dest.writeString(mDefinition);
        dest.writeInt(mCorrectCount);
        dest.writeInt(mIncorrectCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getDefinition() {
        return mDefinition;
    }

    public void setDefinition(String definition) {
        mDefinition = definition;
    }

    public int getCorrectCount() {
        return mCorrectCount;
    }

    public void setCorrectCount(int correctCount) {
        mCorrectCount = correctCount;
    }

    public int getIncorrectCount() {
        return mIncorrectCount;
    }

    public void setIncorrectCount(int incorrectCount) {
        mIncorrectCount = incorrectCount;
    }
}
