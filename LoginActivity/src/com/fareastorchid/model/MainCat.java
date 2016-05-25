package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class MainCat {
    private String id;
    private String cat_name;

    public MainCat() {
        this.id = "";
        this.cat_name = "";
    }

    public MainCat(String id, String catname) {
        this.id = id;
        this.cat_name = catname;
    }

    public MainCat(JSONObject data) {
        try {
            cat_name = (data.isNull("cat_name") ? "" : data
                    .getString("cat_name"));
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

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
