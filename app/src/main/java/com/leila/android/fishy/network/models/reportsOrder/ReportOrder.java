package com.leila.android.fishy.network.models.reportsOrder;

import java.util.ArrayList;

public class ReportOrder {

    public Long order_id;
    public Long user_id;
    public String name;
    public String order_obs;
    public String order_created;
    public String address;
    public String neighborhood;
    public String send_account;
    public String defaulter;
    public String phone;
    public String state;
    public String deliver_date;
    public String delivery_time;
    public ArrayList<ReportItemOrder> items;
    public Double total_amount;
    public Integer priority;

    public Double debt_value;

    public String getUser_name() {
        return name;
    }

    public String getUser_address() {
        return address;
    }

    public ReportOrder(String defaulter,String send_account,String delivery_time,String order_created,String order_obs,Long order_id,Long user_id,String name, String address ,String neighbor,String phone,String deliver_date,Double total_amount, ArrayList<ReportItemOrder> reports,String state,Integer priority){
        this.order_id=order_id;
        this.order_obs=order_obs;
        this.order_created=order_created;
        this.user_id=user_id;
        this.total_amount=total_amount;
        this.name=name;
        this.delivery_time=delivery_time;
        this.address=address;
        this.neighborhood=neighbor;
        this.send_account=send_account;
        this.defaulter=defaulter;
        this.phone=phone;
        this.deliver_date=deliver_date;
        this.state=state;
        this.items=reports;
        this.priority=priority;
    }
}
