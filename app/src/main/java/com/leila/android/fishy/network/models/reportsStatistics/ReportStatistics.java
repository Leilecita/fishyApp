package com.leila.android.fishy.network.models.reportsStatistics;
import java.util.ArrayList;

public class ReportStatistics {

    public Integer month_number;
    public String month;
    public Integer cant_orders_by_month;
    public Double sum_by_month;

    public ArrayList<ProductStatistic> rankingProducts;
    public ArrayList<UserStatistics> rankingClients;


    public ReportStatistics(Integer month_number, String month, Integer cant_orders_by_month, Double sum_by_month, ArrayList<ProductStatistic> topProduct
    ,ArrayList<UserStatistics> topUsers){

        this.cant_orders_by_month=cant_orders_by_month;
        this.month=month;
        this.month_number=month_number;
        this.sum_by_month=sum_by_month;
        this.rankingClients=topUsers;
        this.rankingProducts=topProduct;

    }
}
