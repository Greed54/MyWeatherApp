package com.example.greed54.weatherapp.model;

public class HourlyForecastEntity {
    private int id;
    private String icon;
    private int temp;
    private String dt;

    public HourlyForecastEntity(int id, String icon, int temp, String dt) {
        this.id = id;
        this.icon = icon;
        this.temp = temp;
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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
}
