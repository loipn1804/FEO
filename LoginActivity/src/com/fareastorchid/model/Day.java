package com.fareastorchid.model;

public class Day {
    private String day_no;
    private String month;
    private String week;
    private boolean state;

    public Day() {
        this.day_no = "0";
        this.month = "0";
        this.week = "0";
        this.state = false;
    }

    public Day(String day_no, String month, String week, boolean state) {
        this.day_no = day_no;
        this.month = month;
        this.week = week;
        this.state = state;
    }

    public String getDay_no() {
        return day_no;
    }

    public void setDay_no(String day_no) {
        this.day_no = day_no;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
