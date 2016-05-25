package com.fareastorchid.model;

import org.json.JSONObject;

public class ForecastCountry {

    private String name;
    private String id_forecast;
    private String code;

    public ForecastCountry() {
        this.id_forecast = "";
        this.name = "";
        this.code = "";
    }

    public ForecastCountry(JSONObject data) {
        try {

            id_forecast = (data.isNull("id_forecast") ? "" : data.getString("id_forecast"));
            name = (data.isNull("name") ? "" : data.getString("name"));
            code = (data.isNull("code") ? "" : data.getString("code"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_forecast() {
        return id_forecast;
    }

    public void setId_forecast(String id_forecast) {
        this.id_forecast = id_forecast;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
