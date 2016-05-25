package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Banner {
    private String id;
    private String date_posted;
    private String file_path;
    private String name;
    private String description;


    public Banner() {
        this.id = "";
        this.date_posted = "";
        this.file_path = "";
        this.name = "";
        this.description = "";
    }


    public Banner(JSONObject data) {
        try {
            name = (data.isNull("name") ? "" : data.getString("name"));
            id = (data.isNull("id") ? "" : data.getString("id"));
            date_posted = (data.isNull("date_posted") ? "" : data.getString("date_posted"));
            file_path = (data.isNull("file_path") ? "" : data.getString("file_path"));
            description = (data.isNull("description") ? "" : data.getString("description"));
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

    public String getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
