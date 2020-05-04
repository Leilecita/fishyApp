package com.leila.android.fishy.network.models;

public class Outcome {

    public Long id;
    public String description, created;
    public Double value;

    public Outcome(Double value, String desr){
        this.value=value;
        this.description=desr;
    }
}
