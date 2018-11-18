package com.example.greed54.weatherapp.model;

public class DailyForecastEntity {
    private int id;
    private String icon;
    private int temp_day;
    private int temp_night;
    private int dt;

    public DailyForecastEntity(int id, String icon, int temp_day, int temp_night, int dt) {
        this.id = id;
        this.icon = icon;
        this.temp_day = temp_day;
        this.temp_night = temp_night;
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemp_day() {
        return temp_day;
    }

    public void setTemp_day(int temp_day) {
        this.temp_day = temp_day;
    }

    public int getTemp_night() {
        return temp_night;
    }

    public void setTemp_night(int temp_night) {
        this.temp_night = temp_night;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }
}
