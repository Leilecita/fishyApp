package com.leila.android.fishy.types;

public enum PriceType {
    MAYORISTA(Constants.TYPE_PRICE_MAYORISTA),
    MINORISTA(Constants.TYPE_PRICE_MINORISTA);

    private final String name;

    PriceType(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
