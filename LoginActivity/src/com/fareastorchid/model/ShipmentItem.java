package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ShipmentItem {
    private String id;
    private String code;
    private String name;

    public ShipmentItem() {
        this.id = "";
        this.code = "";
        this.name = "";

    }

    public ShipmentItem(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public ShipmentItem(JSONObject data) {
        try {
            id = (data.isNull("id") ? "" : data.getString("id"));
            name = (data.isNull("name") ? "" : data.getString("name"));
            code = (data.isNull("code") ? "" : data.getString("code"));
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
