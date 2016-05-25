package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderHistory {
    private String order_no;
    private String date;
    private String Contact_person;
    private double total_amount;
    private String delivery_address;
    private String delivery_time;
    private String remark;


    public OrderHistory() {
        this.order_no = "";
        this.date = "";
        this.Contact_person = "";
        this.total_amount = 0;
        this.delivery_address = "";
        this.delivery_time = "";
        this.remark = "";

    }

    public OrderHistory(JSONObject data) {
        try {
            order_no = (data.isNull("order_no") ? "" : data.getString("order_no"));
            date = (data.isNull("date") ? "" : data.getString("date"));
            Contact_person = (data.isNull("Contact_person") ? "" : data.getString("Contact_person"));
            total_amount = (data.isNull("total_amount") ? 0 : data.getDouble("total_amount"));
            delivery_address = (data.isNull("delivery_address") ? "" : data.getString("delivery_address"));
            delivery_time = (data.isNull("delivery_time") ? "" : data.getString("delivery_time"));
            remark = (data.isNull("remark") ? "" : data.getString("remark"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContact_person() {
        return Contact_person;
    }

    public void setContact_person(String Contact_person) {
        this.Contact_person = Contact_person;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
