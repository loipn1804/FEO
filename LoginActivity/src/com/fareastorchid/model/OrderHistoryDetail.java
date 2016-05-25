package com.fareastorchid.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderHistoryDetail {

	private String item_id;
	private double sellingPrice;
	private double basePrice;
	private String img_path;
	private String address;
	private String specReq;
	private int amount;
	private String status;
	private String itemName;
	private String main_color;
	private String trader_price;
	private List<Discount> lsDiscount;

	public OrderHistoryDetail() {
		this.item_id = "";
		this.sellingPrice = 0;
		this.basePrice = 0;
		this.img_path = "";
		this.address = "";
		this.specReq = "";
		this.amount = 0;
		this.itemName = "";
		this.status = "";
		this.main_color = "";
		this.trader_price = "";
		this.lsDiscount = null;
	}

	public OrderHistoryDetail(JSONObject data) {
		try {
			item_id = (data.isNull("id_item") ? "" : data.getString("id_item"));
			img_path = (data.isNull("img_path") ? "" : data.getString("img_path"));
			address = (data.isNull("Office_no") ? "" : data.getString("Office_no"));
			specReq = (data.isNull("special_request") ? "" : data.getString("special_request"));
			amount = (data.isNull("amount") ? 0 : data.getInt("amount"));
			status = (data.isNull("item_status") ? "" : data.getString("item_status"));
			itemName = (data.isNull("item_name") ? "" : data.getString("item_name"));
			main_color = (data.isNull("main_color") ? "" : data.getString("main_color"));
			sellingPrice = (data.isNull("selling_price") ? 0 : data.getDouble("selling_price"));
			basePrice = (data.isNull("base_price") ? 0 : data.getDouble("base_price"));
			trader_price = (data.isNull("trader_price") ? "" : data.getString("trader_price"));
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSpecReq() {
		return specReq;
	}

	public void setSpecReq(String specReq) {
		this.specReq = specReq;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMain_color() {
		return main_color;
	}

	public void setMain_color(String main_color) {
		this.main_color = main_color;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	public List<Discount> getLsDiscount() {
		return lsDiscount;
	}

}
