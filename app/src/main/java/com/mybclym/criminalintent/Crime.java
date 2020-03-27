package com.mybclym.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID miD;
    private Date mDate;
    private String Title;
    private boolean mSolved;

    public Crime(String title, boolean solved) {
        miD = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getMiD() {
        return miD;
    }

    public Date getDate() {
        return mDate;
    }

    public String getTitle() {
        return Title;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
