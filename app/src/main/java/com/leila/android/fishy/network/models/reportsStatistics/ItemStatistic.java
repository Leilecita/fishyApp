package com.leila.android.fishy.network.models.reportsStatistics;

public class ItemStatistic {

        public Long id;
        public String month;
        public Integer quantity_delivery_order;
        public Double total_amount;

        public String created;

        public ItemStatistic(Integer quantity_delivery_order, Double total_amount,String month){

            this.quantity_delivery_order=quantity_delivery_order;
            this.total_amount=total_amount;
            this.month=month;
        }

    }
