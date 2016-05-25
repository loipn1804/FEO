package com.fareastorchid.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailItem {
	private String id;
	private String code_name;
	private String description;
	private String main_color;
	private String country_name;
	private String country_code;
	private String item_status;
	private String cat_code;
	private String cat_name;
	private String tax_amount;
	private String uom_name;
	private String uom_code;
	private String varian_color;
	private String img_path;
	private String item_name;
	private String trader_price;
	private String quantity;
	private String wholesale_price;
	private String quantity_per_uom;
	private List<Discount> lsDiscount;
	private List<FlowerDetailColor> listColors;
	private String updated_on;

	public DetailItem() {
		this.id = "";
		this.code_name = "";
		this.description = "";
		this.main_color = "";
		this.country_name = "";
		this.country_code = "";
		this.item_status = "";
		this.cat_code = "";
		this.cat_name = "";
		this.tax_amount = "";
		this.uom_name = "";
		this.uom_code = "";
		this.varian_color = "";
		this.img_path = "";
		this.item_name = "";
		this.trader_price = "";
		this.quantity = "";
		this.wholesale_price = "";
		this.lsDiscount = null;
		this.listColors = null;
		this.quantity_per_uom = "";
	}

	public DetailItem(String id, String selling_price, String item_name, String img_path, String main_color) {
		this.id = id;
		this.code_name = "";
		this.description = "";
		this.main_color = main_color;
		this.country_name = "";
		this.country_code = "";
		this.item_status = "";
		this.cat_code = "";
		this.cat_name = "";
		this.tax_amount = "";
		this.uom_name = "";
		this.uom_code = "";
		this.varian_color = "";
		this.img_path = img_path;
		this.item_name = item_name;
		this.trader_price = selling_price;
		this.quantity = "";
		this.wholesale_price = "";
		this.lsDiscount = null;
		this.quantity_per_uom = "";
		this.updated_on = "";
	}

	public DetailItem(JSONObject data) {
		try {
			code_name = (data.isNull("code_name") ? "" : data.getString("code_name"));
			id = (data.isNull("id") ? "" : data.getString("id"));
			description = (data.isNull("description") ? "" : data.getString("description"));
			main_color = (data.isNull("main_color") ? "" : data.getString("main_color"));
			country_name = (data.isNull("country_name") ? "" : data.getString("country_name"));
			country_code = (data.isNull("country_code") ? "" : data.getString("country_code"));
			code_name = (data.isNull("code_name") ? "" : data.getString("code_name"));
			cat_code = (data.isNull("cat_code") ? "" : data.getString("cat_code"));
			cat_name = (data.isNull("cat_name") ? "" : data.getString("cat_name"));
			tax_amount = (data.isNull("tax_amount") ? "" : data.getString("tax_amount"));
			uom_name = (data.isNull("uom_name") ? "" : data.getString("uom_name"));
			uom_code = (data.isNull("uom_code") ? "" : data.getString("uom_code"));
			varian_color = (data.isNull("varian_color") ? "" : data.getString("varian_color"));
			item_status = (data.isNull("item_status") ? "" : data.getString("item_status"));
			img_path = (data.isNull("img_path") ? "" : data.getString("img_path"));
			item_name = (data.isNull("item_name") ? "" : data.getString("item_name"));
			trader_price = (data.isNull("trader_price") ? "" : data.getString("trader_price"));
			quantity = (data.isNull("quantity") ? "" : data.getString("quantity"));
			wholesale_price = (data.isNull("wholesale_price") ? "" : data.getString("wholesale_price"));
			quantity_per_uom = (data.isNull("quantity_per_uom") ? "" : data.getString("quantity_per_uom"));
			updated_on = (data.isNull("updated_on") ? "" : data.getString("updated_on"));
			if (!data.isNull("discounts") && data.getJSONArray("discounts").length() > 0) {
				JSONArray discountJRR = data.getJSONArray("discounts");
				this.lsDiscount = new ArrayList<Discount>();
				for (int i = 0; i < discountJRR.length(); i++) {
					JSONObject discountObj = discountJRR.getJSONObject(i);
					Discount discount = new Discount(discountObj);
					this.lsDiscount.add(discount);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public List<FlowerDetailColor> getListColors() {
		return listColors;
	}

	public void setListColors(List<FlowerDetailColor> listColors) {
		this.listColors = listColors;
	}

	public String getQuantity_per_uom() {
		return quantity_per_uom;
	}

	public void setQuantity_per_uom(String quantity_per_uom) {
		this.quantity_per_uom = quantity_per_uom;
	}

	public List<Discount> getLsDiscount() {
		return lsDiscount;
	}

	public void setLsDiscount(List<Discount> lsDiscount) {
		this.lsDiscount = lsDiscount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode_name() {
		return code_name;
	}

	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getItem_status() {
		return item_status;
	}

	public void setItem_status(String item_status) {
		this.item_status = item_status;
	}

	public String getCat_code() {
		return cat_code;
	}

	public void setCat_code(String cat_code) {
		this.cat_code = cat_code;
	}

	public String getCat_name() {
		return cat_name;
	}

	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}

	public String getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(String tax_amount) {
		this.tax_amount = tax_amount;
	}

	public String getUom_name() {
		return uom_name;
	}

	public void setUom_name(String uom_name) {
		this.uom_name = uom_name;
	}

	public String getUom_code() {
		return uom_code;
	}

	public void setUom_code(String uom_code) {
		this.uom_code = uom_code;
	}

	public String getVarian_color() {
		return varian_color;
	}

	public void setVarian_color(String varian_color) {
		this.varian_color = varian_color;
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getWholesale_price() {
		return wholesale_price;
	}

	public void setWholesale_price(String wholesale_price) {
		this.wholesale_price = wholesale_price;
	}
	
	public String getUpdateOn() {
		return updated_on;
	}
}
