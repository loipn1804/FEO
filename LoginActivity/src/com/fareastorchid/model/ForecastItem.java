package com.fareastorchid.model;

import org.json.JSONObject;

public class ForecastItem {
    private String id_forecast;
    private String date_arrival;
    private String country;
    private String item_name;
    private String img_path;
    private String trader_price;
    private String wholesale_price;
    private String main_color;
    private String varian_color;

    public ForecastItem() {
        this.main_color = "";
        this.varian_color = "";
        this.country = "";
        this.date_arrival = "";
        this.id_forecast = "";
        this.img_path = "";
        this.item_name = "";
        this.trader_price = "";
        this.wholesale_price = "";
    }

    public ForecastItem(JSONObject data) {
        try {
            id_forecast = (data.isNull("id_forecast") ? "" : data.getString("id_forecast"));
            country = (data.isNull("country") ? "" : data.getString("country"));
            date_arrival = (data.isNull("date_arrival") ? "" : data.getString("date_arrival"));
            img_path = (data.isNull("img_path") ? "" : data.getString("img_path"));
            item_name = (data.isNull("item_name") ? "" : data.getString("item_name"));
            trader_price = (data.isNull("trader_price") ? "" : data.getString("trader_price"));
            wholesale_price = (data.isNull("wholesale_price") ? "" : data.getString("wholesale_price"));
            main_color = (data.isNull("main_color") ? "" : data.getString("main_color"));
            varian_color = (data.isNull("varian_color") ? "" : data.getString("varian_color"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMain_color() {
        return main_color;
    }

    public void setMain_color(String main_color) {
        this.main_color = main_color;
    }

    public String getVarian_color() {
        return varian_color;
    }

    public void setVarian_color(String varian_color) {
        this.varian_color = varian_color;
    }

    public String getId_forecast() {
        return id_forecast;
    }

    public void setId_forecast(String id_forecast) {
        this.id_forecast = id_forecast;
    }

    public String getDate_arrival() {
        return date_arrival;
    }

    public void setDate_arrival(String date_arrival) {
        this.date_arrival = date_arrival;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getTrader_price() {
        return trader_price;
    }

    public void setTrader_price(String trader_price) {
        this.trader_price = trader_price;
    }

    public String getWholesale_price() {
        return wholesale_price;
    }

    public void setWholesale_price(String wholesale_price) {
        this.wholesale_price = wholesale_price;
    }

}
