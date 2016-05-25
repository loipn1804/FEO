package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Color {

    private String id;
    private String main_color;

    public Color() {
        this.id = "";
        this.main_color = "";
    }

    public Color(JSONObject data) {
        try {
            id = (data.isNull("id") ? "" : data.getString("id"));
            main_color = (data.isNull("main_color") ? "" : data.getString("main_color"));
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

    public String getMain_color() {
        return main_color;
    }

    public void setMain_color(String main_color) {
        this.main_color = main_color;
    }
}
