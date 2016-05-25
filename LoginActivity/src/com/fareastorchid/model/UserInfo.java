package com.fareastorchid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
	private String id;
	private String email;
	private String username;
	private String full_name;
	private String status;
	private String office_no;
	private String billing_address;
	private String contact_number;
	private String level;
	private String position;
	private String shop_name;
	private String outlet;

	public UserInfo() {
		this.id = "";
		this.email = "";
		this.full_name = "";
		this.status = "";
		this.username = "";
		this.billing_address = "";
		this.office_no = "";
		this.contact_number = "";
		this.level = "";
		this.shop_name = "";
		this.outlet = "";
	}

	public UserInfo(JSONObject data) {
		try {
			username = (data.isNull("username") ? "" : data
					.getString("username"));
			id = (data.isNull("id") ? "" : data.getString("id"));
			email = (data.isNull("email") ? "" : data.getString("email"));
			full_name = (data.isNull("contact_person") ? "" : data
					.getString("contact_person"));
			status = (data.isNull("status") ? "" : data.getString("status"));
			office_no = (data.isNull("office_no") ? "" : data
					.getString("office_no"));
			billing_address = (data.isNull("billing_address") ? "" : data
					.getString("billing_address"));
			contact_number = (data.isNull("mobile_no") ? "" : data
					.getString("mobile_no"));
			level = (data.isNull("level") ? "" : data.getString("level"));
			position = (data.isNull("position") ? "" : data
					.getString("position"));
			shop_name = (data.isNull("company_name") ? "" : data
					.getString("company_name"));
			JSONArray jOutlet = (data.isNull("outlets") ? null : data
					.getJSONArray("outlets"));
			outlet = jOutlet.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getOffice_no() {
		return office_no;
	}

	public void setOffice_no(String office_no) {
		this.office_no = office_no;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getBilling_address() {
		return billing_address;
	}

	public void setBilling_address(String billing_address) {
		this.billing_address = billing_address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return full_name;
	}

	public void setFullName(String name) {
		this.full_name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOfficeNo() {
		return office_no;
	}

	public void setOfficeNo(String officeNo) {
		this.office_no = officeNo;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getOutlet() {
		return outlet;
	}
}
