package com.example.e_farmpolandedition;

import com.google.android.gms.maps.model.LatLng;

public class uprawa {
    String latitude;
    String longtitude;
    String name;
    String plantName;
    String dataRozpoczecia;
    String surface;
    String surfaceMetric;
    String description;

    public uprawa() {
    }

    public uprawa(String latitude, String longtitude, String name, String plantName, String dataRozpoczecia,
                  String surface, String surfaceMetric, String description) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
        this.plantName = plantName;
        this.dataRozpoczecia = dataRozpoczecia;
        this.surface = surface;
        this.surfaceMetric = surfaceMetric;
        this.description = description;
    }

    public String getDataRozpoczecia() {
        return dataRozpoczecia;
    }

    public void setDataRozpoczecia(String dataRozpoczecia) {
        this.dataRozpoczecia = dataRozpoczecia;
    }

    public String getSurfaceMetric() {
        return surfaceMetric;
    }

    public void setSurfaceMetric(String surfaceMetric) {
        this.surfaceMetric = surfaceMetric;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
