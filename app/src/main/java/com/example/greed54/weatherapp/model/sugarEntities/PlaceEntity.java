package com.example.greed54.weatherapp.model.sugarEntities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "place_entity")
public class PlaceEntity{

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "name_place")
    private String namePlace;
    @Property(nameInDb = "longitude")
    private double longitude;
    @Property(nameInDb = "latitude")
    private double latitude;

    public PlaceEntity() {
    }

    public PlaceEntity(String namePlace, double longitude, double latitude) {
        this.namePlace = namePlace;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Generated(hash = 1553574638)
    public PlaceEntity(Long id, String namePlace, double longitude,
            double latitude) {
        this.id = id;
        this.namePlace = namePlace;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return namePlace;
    }

    public void setName(String namePlace) {
        this.namePlace = namePlace;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getNamePlace() {
        return this.namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }
}
