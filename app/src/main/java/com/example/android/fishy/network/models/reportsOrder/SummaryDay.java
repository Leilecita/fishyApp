package com.example.android.fishy.network.models.reportsOrder;

public class SummaryDay {

    public Long productId;
    public String nameProduct;
    public Double totalQuantity;
    public Double price;

    public SummaryDay(Long idProduct,String nameProduct,Double totalQuantity,Double price ){
        this.productId=idProduct;
        this.nameProduct=nameProduct;
        this.totalQuantity=totalQuantity;
        this.price=price;
    }
}
