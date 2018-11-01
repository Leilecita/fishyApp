package com.example.android.fishy.Events;

public class EventOrderState {
    public Long mIdUser;
    public String mState; //created or deleted

    public EventOrderState(Long message,String state) {
        mIdUser = message;
        mState=state;
    }

    public Long getIdUser() {
        return mIdUser;
    }

    public String getState() {
        return mState;
    }
}
