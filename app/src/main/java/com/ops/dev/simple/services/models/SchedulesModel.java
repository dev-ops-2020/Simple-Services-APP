package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class SchedulesModel implements Serializable {
    
    private String day;
    private String time;

    public SchedulesModel() {
        this.day = "";
        this.time = "";
    }

    public SchedulesModel(String day, String time) {
        this.day = day;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}