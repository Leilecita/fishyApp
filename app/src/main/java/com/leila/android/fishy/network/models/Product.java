package com.leila.android.fishy.network.models;

/**
 * Created by leila on 17/7/18.
 */

public class Product {

    public Long id;
    public String fish_name;
    public Double price;
    public Double wholesaler_price;
    public Double stock;
    public Double product_cost;

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

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Product(String fish_name, Double price,Double wholesaler_price, Double stock,Double cost){
        this.fish_name=fish_name;
        this.price=price;
        this.wholesaler_price=wholesaler_price;
        this.stock=stock;
        this.product_cost=cost;

    }
}
