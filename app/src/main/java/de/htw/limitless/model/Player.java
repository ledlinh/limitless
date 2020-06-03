package de.htw.limitless.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "player")
public class Player {

    @PrimaryKey(autoGenerate = true)
    public int pid; //Player ID

    private String mName;
    private int mLevel;

    @Ignore
    private String[] mTitleList = new String[]{"Not There", "Baby", "Smart Kid", "Clever Teenager", "Genius Youngster", "Overachiever", "Scholar", "Pioneer", "Inventor", "Walking Intelligence", "Einstein"};

    @Ignore
    private String mTitle;

    private int mCookies;
    private int mQuestionsAnswered;

    private int mCupcakes;

    public Player(int pid, String name) {
        this.pid = pid;
        this.mName = name;
        this.mLevel = 0;
        this.mCookies = 0;
        this.mQuestionsAnswered = 0;
        this.mCupcakes = 0;
    }

    @Ignore
    public Player(String name) {
        this.mName = name;
        this.mLevel = 0;
        this.mTitle = mTitleList[mLevel];
        this.mCookies = 0;
        this.mQuestionsAnswered = 0;
        this.mCupcakes = 0;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public String getTitle() {
        this.mTitle = mTitleList[mLevel];
        return mTitle;
    }

    public void answeredOneQuestion() {
        mCookies++;
        mQuestionsAnswered++;
    }

    public int getQuestionsAnswered() {
        return mQuestionsAnswered;
    }

    public void setQuestionsAnswered(int mQuestionsAnswered) {
        this.mQuestionsAnswered = mQuestionsAnswered;
    }

    public void levelUp() {
        mLevel++;
        mCupcakes++;
        mTitle = mTitleList[mLevel];
    }

    public int getCookies() {
        return mCookies;
    }

    public void setCookies(int mCookies) {
        this.mCookies = mCookies;
    }

    public int getCupcakes() {
        return mCupcakes;
    }

    public void setCupcakes(int mCupcakes) {
        this.mCupcakes = mCupcakes;
    }

}
