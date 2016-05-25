package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CateType {
    private String id;
    private String code;
    private String name;
    private String parent_cat;

    public CateType() {
        this.id = "";
        this.code = "";
        this.name = "";
        this.parent_cat = "";
    }

    public CateType(String id, String name) {
        this.id = id;
        this.code = "";
        this.name = name;
        this.parent_cat = "";
    }

    public CateType(JSONObject data) {
        try {
            name = (data.isNull("name") ? "" : data.getString("name"));
            id = (data.isNull("id") ? "" : data.getString("id"));
            code = (data.isNull("code") ? "" : data.getString("code"));
            parent_cat = (data.isNull("parent_cat") ? "" : data.getString("parent_cat"));
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

    public String getParent_cat() {
        return parent_cat;
    }

    public void setParent_cat(String parent_cat) {
        this.parent_cat = parent_cat;
    }
}
