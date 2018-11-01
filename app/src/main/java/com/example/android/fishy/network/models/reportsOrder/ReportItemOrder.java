package com.example.android.fishy.network.models.reportsOrder;

public class ReportItemOrder {
    public String fish_name;
    public Double price;
    public Double quantity;

    public  ReportItemOrder(String fish_name,Double price,Double quantity){
        this.fish_name=fish_name;
        this.price=price;
        this.quantity=quantity;
    }
}
