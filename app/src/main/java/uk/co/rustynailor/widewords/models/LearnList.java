package uk.co.rustynailor.widewords.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by russellhicks on 12/11/2016.
 */

public class LearnList implements Parcelable{

    //quiz questions
    private ArrayList<Integer> mWordList;
    //position in list
    private int mWordListPosition;


    //empty constructor
    public LearnList() {
        mWordList = new ArrayList<>();
        mWordListPosition = 0;
    }

    protected LearnList(Parcel in) {
        mWordListPosition = in.readInt();
        mWordList = new ArrayList<Integer>();
        in.readList(mWordList, Integer.class.getClassLoader());
    }

    public static final Creator<LearnList> CREATOR = new Creator<LearnList>() {
        @Override
        public LearnList createFromParcel(Parcel in) {
            return new LearnList(in);
        }

        @Override
        public LearnList[] newArray(int size) {
            return new LearnList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mWordListPosition);
        parcel.writeList(mWordList);
    }

    public ArrayList<Integer> getWordList() {
        return mWordList;
    }

    public void setWordList(ArrayList<Integer> wordList) {
        mWordList = wordList;
    }

    public int getWordListPosition() {
        return mWordListPosition;
    }

    public void setWordListPosition(int wordListPosition) {
        mWordListPosition = wordListPosition;
    }

    public int getCurrentWordId()
    {
        return mWordList.get(mWordListPosition);
    }

    public boolean nextWord(){
        boolean success;
        if((mWordListPosition + 1) >= mWordList.size()){
            success =  false;
        } else {
            mWordListPosition++;
            success =  true;
        }
        return success;
    }

    public boolean previousWord(){
        boolean success;
        if((mWordListPosition - 1) < 0){
            success =  false;
        } else {
            mWordListPosition--;
            success =  true;
        }
        return success;
    }
}
