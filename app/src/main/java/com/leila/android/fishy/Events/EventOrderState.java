package com.leila.android.fishy.Events;

public class EventOrderState {
    public Long mIdUser;
    public String mState; //created or deleted
    public String mDate;

    public EventOrderState(Long id,String state,String date) {
        mIdUser = id;
        mState=state;
        mDate=date;
    }

    public Long getIdUser() {
        return mIdUser;
    }

    public String getState() {
        return mState;
    }
    public String getDate() {
        return mDate;
    }
}
