package com.fareastorchid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Forecast {
    private List<ForecastCountry> listCountries;
    private String date_arrival;

    public Forecast() {
        this.listCountries = null;
        this.date_arrival = "";
    }

    public Forecast(JSONObject data) {
        try {
            date_arrival = (data.isNull("date_arrival") ? "" : data
                    .getString("date_arrival"));
            JSONArray list = (data.isNull("countries") ? null : data
                    .getJSONArray("countries"));
            listCountries = new ArrayList<ForecastCountry>();
            for (int i = 0; i < list.length(); i++) {
                ForecastCountry country = new ForecastCountry(
                        list.getJSONObject(i));
                listCountries.add(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<ForecastCountry> getListCountries() {
        return listCountries;
    }

    public void setListCountries(List<ForecastCountry> listCountries) {
        this.listCountries = listCountries;
    }

    public String getDateArrival() {
        return date_arrival;
    }

    public void setDateArrival(String date_arrival) {
        this.date_arrival = date_arrival;
    }
}
