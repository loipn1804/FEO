package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DelAddress {

    private String id;
    private String userId;
    private String address;
    private String contact;
    private boolean state;

    public DelAddress() {
        this.id = "";
        this.userId = "";
        this.address = "";
    }

    public DelAddress(JSONObject data) {
        try {
            id = (data.isNull("id") ? "" : data.getString("id"));
            userId = (data.isNull("id_user") ? "" : data.getString("id_user"));
            address = (data.isNull("address") ? "" : data.getString("address"));
            contact = (data.isNull("contact") ? "" : data.getString("contact"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    
    public String getContact() {
        return contact;
    }
}
