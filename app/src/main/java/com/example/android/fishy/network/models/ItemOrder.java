package com.example.android.fishy.network.models;

/**
 * Created by leila on 17/7/18.
 */

public class ItemOrder {
    public Long id;
    public Long product_id;
    public Long order_id;
    public Double quantity;
    public String created;

    public ItemOrder(Long product_id, Long order_id, Double quantity){
        this.product_id=product_id;
        this.order_id=order_id;
        this.quantity=quantity;

    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
