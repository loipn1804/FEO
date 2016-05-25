package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class FlowerDetailColor {
    private String id_item;
    private String name;


    public FlowerDetailColor() {
        this.id_item = "0";
        this.name = "0";
    }

    public FlowerDetailColor(JSONObject data) {
        try {
            id_item = (data.isNull("id_item") ? "" : data.getString("id_item"));
            name = (data.isNull("name") ? "" : data.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
