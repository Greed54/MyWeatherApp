package com.example.greed54.weatherapp.model.forecast;

public class Wind {
    private double speed;
    private double deg;

    public Wind() {
    }

    public int getSpeed() {
        return (int)speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}
