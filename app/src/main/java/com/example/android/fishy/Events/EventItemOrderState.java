package com.example.android.fishy.Events;

public class EventItemOrderState {

    public Long mId;
    public Double quantity;
    public Long order_id;
    public Long product_id;

    public EventItemOrderState(Long id,Long product_id,Double quantity, Long order_id) {
        mId = id;
        this.quantity=quantity;
        this.order_id=order_id;
        this.product_id=product_id;
    }

    public Long getId() {
        return mId;
    }

}
