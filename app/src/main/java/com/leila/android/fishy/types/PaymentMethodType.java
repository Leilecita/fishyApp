package com.leila.android.fishy.types;

public enum PaymentMethodType {
    CASH(Constants.TYPE_PAYMENT_CASH),
    TRANSFER(Constants.TYPE_PAYMENT_TRANSFER),
    CARD(Constants.TYPE_PAYMENT_CARD);

    private final String name;

    PaymentMethodType(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
