package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Discount {
    private String id;
    private String name;
    private String amount;
    private String total_item;

    public Discount() {
        this.id = "";
        this.amount = "0";
        this.total_item = "0";
        this.name = "";
    }

    public Discount(JSONObject data) {
        try {
            id = (data.isNull("id") ? "" : data.getString("id"));
            amount = (data.isNull("amount") ? "0" : data.getString("amount"));
            total_item = (data.isNull("qty") ? "0" : data.getString("qty"));
            name = (data.isNull("name") ? "0" : data.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }
}
