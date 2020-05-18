package com.leila.android.fishy.network.models;

public class Outcome {

    public Long id;
    public String type,description, created;
    public Double value;

    public Outcome(Double value,String type, String desr){
        this.value=value;
        this.type=type;
        this.description=desr;
    }
}
