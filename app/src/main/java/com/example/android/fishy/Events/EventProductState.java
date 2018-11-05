package com.example.android.fishy.Events;

public class EventProductState {

    public Long mIdProduct;
    public Double mStock;
    public String mState; //created / deleted / edited

    public EventProductState(Long id, String state, Double stock) {
        mIdProduct = id;
        mState=state;
        mStock=stock;
    }

    public Long getIdProduct() {
        return mIdProduct;
    }

    public String getState() {
        return mState;
    }

    public Double getStock() {
        return mStock;
    }
}
