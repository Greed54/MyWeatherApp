package com.example.greed54.weatherapp.model.forecast;

public class Main {
    private double temp;
    private double pressure;
    private int humidity;
    private double temp_min;
    private double temp_max;

    public Main() {
    }

    public int getTemp() {
        return (int)temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return (int)pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
}
