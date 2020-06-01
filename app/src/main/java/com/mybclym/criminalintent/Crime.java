package com.mybclym.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mID;
    private Date mDate;
    private String mTitle;
    private boolean mSolved;
    private boolean mPoliceRequires;
    private String mSuspect;
    private String mNumber;

    public Crime(UUID ID) {
        mID = ID;
        mDate = new Date();
    }

    public Crime() {
        this(UUID.randomUUID());
    }

    public UUID getID() {
        return mID;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isPoliceRequires() {
        return mPoliceRequires;
    }

    public void setPoliceRequires(boolean policeRequires) {
        mPoliceRequires = policeRequires;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }
}
