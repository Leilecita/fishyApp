package com.leila.android.fishy.network.models;

public class Income {

    public String created;
    public String description,type;
    public Double value;
    public Long id;

    public Income(String descr,String type, Double value,String created){

        this.type=type;
        this.description=descr;
        this.created=created;
        this.value=value;
    }
}


