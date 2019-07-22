package com.leila.android.fishy.network.models;

public class Neighborhood {

   public String name;
    public String created;
    public Long id;

    public Neighborhood(String name){
        this.name=name;
    }

    public String getName(){return this.name;}
}
