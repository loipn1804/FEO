package com.fareastorchid.model;

public class OrderItem {

    private DetailItem item;
    private String id;
    private int quantity;
    private double sellingPrice;
    private double basePrice;
    private String address;
    private String specReq;
    private String timeSlot;

    public OrderItem() {
        this.item = null;
        this.id = "";
        this.quantity = 0;
        this.sellingPrice = 0;
        this.basePrice = 0;
        this.address = "";
        this.specReq = "";
        this.timeSlot = "";
    }

    public OrderItem(DetailItem item, String id, int quantity, double sellingPrice, double basePrice, String address, String specReq, String timeSlot) {
        this.item = item;
        this.id = id;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.basePrice = basePrice;
        this.address = address;
        this.specReq = specReq;
        this.timeSlot = timeSlot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DetailItem getItem() {
        return item;
    }

    public void setItem(DetailItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
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

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

}
