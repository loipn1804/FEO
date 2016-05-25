package com.fareastorchid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeItem {
    private String id;
    private String url;
    private String name;
    private String traderPrice;
    private String wholesalePrice;
    private String status;
    private String quantity;
    private String cat_name;
    private String country_name;
    private String country_code;
    private String main_color;
    private List<FlowerDetailColor> listColors;

    public HomeItem() {
        this.id = "";
        this.url = "";
        this.name = "";
        this.traderPrice = "";
        this.wholesalePrice = "";
        this.status = "";
        this.quantity = "";
        this.cat_name = "";
        this.country_name = "";
        this.country_code = "";
        this.listColors = null;
        this.main_color = "";
    }

    public HomeItem(JSONObject data) {
        try {
            name = (data.isNull("item_name") ? "" : data.getString("item_name"));
            id = (data.isNull("id") ? "" : data.getString("id"));
            url = (data.isNull("img_path") ? "" : data.getString("img_path"));
            traderPrice = (data.isNull("trader_price") ? "" : data
                    .getString("trader_price"));
            wholesalePrice = (data.isNull("wholesale_price") ? "" : data
                    .getString("wholesale_price"));
            status = (data.isNull("item_status") ? "" : data
                    .getString("item_status"));
            quantity = (data.isNull("quantity_per_uom") ? "" : data
                    .getString("quantity_per_uom"));
            cat_name = (data.isNull("cat_name") ? "" : data
                    .getString("cat_name"));
            country_name = (data.isNull("country_name") ? "" : data
                    .getString("country_name"));
            country_code = (data.isNull("country_code") ? "" : data
                    .getString("country_code"));
            main_color = (data.isNull("main_color") ? "" : data
                    .getString("main_color"));
            if (!data.isNull("colors")
                    && data.getJSONArray("colors").length() > 0) {
                JSONArray colorJRR = data.getJSONArray("colors");
                this.listColors = new ArrayList<FlowerDetailColor>();
                for (int i = 0; i < colorJRR.length(); i++) {
                    JSONObject colorObj = colorJRR.getJSONObject(i);
                    FlowerDetailColor color = new FlowerDetailColor(colorObj);
                    this.listColors.add(color);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getMain_color() {
        return main_color;
    }

    public void setMain_color(String main_color) {
        this.main_color = main_color;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public List<FlowerDetailColor> getListColors() {
        return listColors;
    }

    public void setListColors(List<FlowerDetailColor> listColors) {
        this.listColors = listColors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public String getTraderPrice() {
        return traderPrice;
    }

    public void setTraderPrice(String price) {
        this.traderPrice = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}