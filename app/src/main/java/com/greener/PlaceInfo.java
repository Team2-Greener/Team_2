package com.greener;

import java.io.Serializable;

public class PlaceInfo implements Serializable {

    private double mLat, mLon;
    private String mDivision;
    private String mTitle;
    private String mDescription;

    public PlaceInfo(double lat, double lon, String division, String title, String description) {
        mLat = lat;
        mLon = lon;
        mDivision = division;
        mTitle = title;
        mDescription = description;
    }

    public double getLatitude() {
        return mLat;
    }

    public double getLongitude() {
        return mLon;
    }

    public String getDivision() {
        return mDivision;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

}