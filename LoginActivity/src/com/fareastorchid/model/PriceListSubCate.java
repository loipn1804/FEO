package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PriceListSubCate {
    private String id;
    private String name;

    public PriceListSubCate() {
        this.id = "";
        this.name = "";
    }

    public PriceListSubCate(JSONObject data) {
        try {
            id = (data.isNull("id") ? "" : data.getString("id"));
            name = (data.isNull("name") ? "" : data.getString("name"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
