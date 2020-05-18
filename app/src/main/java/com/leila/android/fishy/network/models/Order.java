package com.leila.android.fishy.network.models;

/**
 * Created by leila on 17/7/18.
 */

public class Order {

    public Long id;
    public Long user_id;
    public String deliver_date;
    public String delivery_time;
    public String send_account;
    public String defaulter;
    public String observation;
    public String state;
    public Integer priority;
    public String created;
    public Double debt_value;

    public Order(Long user_id, String deliver_date, String observation,String state,String delivery_time){
        this.user_id=user_id;
        this.deliver_date=deliver_date;
        this.observation=observation;
        this.state=state;
        this.delivery_time=delivery_time;
        this.send_account="false";
        this.defaulter="false";
        this.delivery_time=delivery_time;

    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getDeliver_date() {
        return deliver_date;
    }

    public void setDeliver_date(String deliver_date) {
        this.deliver_date = deliver_date;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
