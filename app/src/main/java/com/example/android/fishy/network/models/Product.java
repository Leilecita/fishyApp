package com.example.android.fishy.network.models;

/**
 * Created by leila on 17/7/18.
 */

public class Product {

    public Long id;
    public String fish_name;
    public Double price;
    public Integer stock;

    public String getFish_name() {
        return fish_name;
    }

    public void setFish_name(String fish_name) {
        this.fish_name = fish_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Product(String fish_name, Double price, Integer stock){
        this.fish_name=fish_name;
        this.price=price;
        this.stock=stock;

    }
}
