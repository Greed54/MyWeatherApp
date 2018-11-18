package com.example.greed54.weatherapp;

import android.app.Application;

import com.example.greed54.weatherapp.dbWorking.DbOpenHelper;
import com.example.greed54.weatherapp.model.sugarEntities.DaoMaster;
import com.example.greed54.weatherapp.model.sugarEntities.DaoSession;

public class WeatherApp extends Application {

    private DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        daoSession = new DaoMaster(new DbOpenHelper(this, "places.db").getWritableDb()).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
