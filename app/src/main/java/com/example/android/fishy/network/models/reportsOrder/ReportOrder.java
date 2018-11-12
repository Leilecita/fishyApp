package com.example.android.fishy.network.models.reportsOrder;

import java.util.ArrayList;

public class ReportOrder {

    public Long order_id;
    public Long user_id;
    public String name;
    public String order_obs;
    public String order_created;
    public String address;
    public String neighborhood;
    public String phone;
    public String state;
    public String deliver_date;
    public ArrayList<ReportItemOrder> items;
    public Double total_amount;
    public Integer priority;

    public String getUser_name() {
        return name;
    }

    public String getUser_address() {
        return address;
    }

    public ReportOrder(String order_created,String order_obs,Long order_id,Long user_id,String name, String address ,String neighbor,String phone,String deliver_date,Double total_amount, ArrayList<ReportItemOrder> reports,String state,Integer priority){
        this.order_id=order_id;
        this.order_obs=order_obs;
        this.order_created=order_created;
        this.user_id=user_id;
        this.total_amount=total_amount;
        this.name=name;
        this.address=address;
        this.neighborhood=neighbor;
        this.phone=phone;
        this.deliver_date=deliver_date;
        this.state=state;
        this.items=reports;
        this.priority=priority;
    }
}
