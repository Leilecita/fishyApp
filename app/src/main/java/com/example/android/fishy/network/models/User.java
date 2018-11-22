package com.example.android.fishy.network.models;

/**
 * Created by leila on 17/7/18.
 */


public class User {

    public Long id;
    public String name;
    public String address;
    public String phone;
    public String document;
    public String neighborhood;
    public String image_url;
    public Integer pendient_orders;
    public String created;
    public String defaulter;

    public String imageData;

    public String havePendientOrder;

    public User(String name, String address, String phone, String document,String picpath, String neighboor,Integer pendient_orders){
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.document=document;
        this.image_url=picpath;
        this.neighborhood=neighboor;
        this.pendient_orders=pendient_orders;
        this.defaulter="false";

    }
    public String getImageUrl(){
        return this.image_url;
    }
    public String getNeighborhood() {
        return neighborhood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }



}
