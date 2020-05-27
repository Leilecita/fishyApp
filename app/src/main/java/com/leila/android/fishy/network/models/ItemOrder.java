package com.leila.android.fishy.network.models;

/**
 * Created by leila on 17/7/18.
 */

public class ItemOrder {
    public Long id;
    public Long product_id;
    public Long order_id;
    public Double quantity;
    public String created;
    public String product_name;
    public Double product_cost;
    public String price_type;
    public Double price;

    public String new_version;

    public ItemOrder(Long product_id, Long order_id, Double quantity,String product_name,String price_type,Double price, Double product_cost){
        this.product_id=product_id;
        this.order_id=order_id;
        this.quantity=quantity;
        this.price_type=price_type;
        this.product_name=product_name;
        this.product_cost=product_cost;
        this.price=price;
        this.new_version="true";
    }

    public Long getProduct_id() {
        return product_id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
