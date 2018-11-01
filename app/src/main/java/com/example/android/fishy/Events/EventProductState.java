package com.example.android.fishy.Events;

public class EventProductState {

    public Long mIdProduct;
    public Integer mStock;
    public String mState; //created / deleted / edited

    public EventProductState(Long message, String state, Integer stock) {
        mIdProduct = message;
        mState=state;
        mStock=stock;
    }

    public Long getIdProduct() {
        return mIdProduct;
    }

    public String getState() {
        return mState;
    }

    public Integer getStock() {
        return mStock;
    }
}
