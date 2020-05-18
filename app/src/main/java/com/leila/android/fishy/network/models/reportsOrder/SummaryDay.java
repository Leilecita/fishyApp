package com.leila.android.fishy.network.models.reportsOrder;

public class SummaryDay {

    public Long productId;
    public String nameProduct;
    public Double totalQuantity;
    public Double totalPrice;

    public SummaryDay(Long idProduct,String nameProduct,Double totalQuantity,Double totalPrice ){
        this.productId=idProduct;
        this.nameProduct=nameProduct;
        this.totalQuantity=totalQuantity;
        this.totalPrice=totalPrice;
    }
}
