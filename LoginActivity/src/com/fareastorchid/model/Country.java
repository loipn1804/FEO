package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Country {
    private String id;
    private String name;
    private String code;

    public Country() {
        this.id = "";
        this.name = "";
        this.code = "";
    }

    public Country(JSONObject data) {
        try {
            name = (data.isNull("item_name") ? "" : data.getString("item_name"));
            code = (data.isNull("code") ? "" : data.getString("code"));
            id = (data.isNull("id") ? "" : data.getString("id"));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}