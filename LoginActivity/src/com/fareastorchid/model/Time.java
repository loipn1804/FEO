package com.fareastorchid.model;

public class Time {
    private String time;
    private boolean state;

    public Time() {
        this.time = "";
        this.state = false;
    }

    public Time(String time, boolean state) {
        this.time = time;
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
