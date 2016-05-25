package com.fareastorchid.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchItem {
    private String id;
    private String img_path;
    private String item_name;
    private String trader_price;
    private String wholesale_price;

    public SearchItem() {
        this.id = "";
        this.img_path = "";
        this.item_name = "";
        this.trader_price = "";
        this.wholesale_price = "";
    }

    public SearchItem(JSONObject data) {
        try {
            id = (data.isNull("id") ? "" : data.getString("id"));
            img_path = (data.isNull("img_path") ? "" : data.getString("img_path"));
            item_name = (data.isNull("item_name") ? "" : data.getString("item_name"));
            trader_price = (data.isNull("trader_price") ? "" : data.getString("trader_price"));
            wholesale_price = (data.isNull("wholesale_price") ? "" : data.getString("wholesale_price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getWholesale_price() {
        return wholesale_price;
    }

    public void setWholesale_price(String wholesale_price) {
        this.wholesale_price = wholesale_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getTrader_price() {
        return trader_price;
    }

    public void setTrader_price(String trader_price) {
        this.trader_price = trader_price;
    }
}
