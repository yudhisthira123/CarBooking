package com.example.yudhisthira.carbooking.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yudhisthira on 15/05/17.
 */

public class Car implements Serializable, Comparable<Car> {

    @SerializedName("name")
    private String              mCarName;

    @SerializedName("shortDescription")
    private String              mShortDescription;

    @SerializedName("id")
    private int                 mCarID;

    @SerializedName("description")
    private String              mCarDescription;

    @SerializedName("image")
    private String              mCarImage;

    private String              mCarBookingDate;
    private String              mCarBookingTime;
    private int                 mCarBookingDuration;

    public String getCarName() {
        return mCarName;
    }

    public void setCarName(String carName) {
        mCarName = carName;
    }

    public String getCarShortDescription() {
        return mShortDescription;
    }

    public void setCarShortDescription(String carDescription) {
        mShortDescription = carDescription;
    }

    public String getCarDescription() {
        return mCarDescription;
    }

    public void setCarDescription(String carDescription) {
        mCarDescription = carDescription;
    }

    public int getCarID() {
        return mCarID;
    }

    public void setCarID(int carID) {
        mCarID = carID;
    }

    public String getImagePath() {
        return mCarImage;
    }

    public void setImagePath(String carImage) {
        mCarImage = carImage;
    }

    public void setCarBookingDate(String carBookingDate) {
        mCarBookingDate = carBookingDate;
    }

    public String getCarBookingDate() {
        return mCarBookingDate;
    }

    public void setCarBookingTime(String carBookingTime) {
        mCarBookingTime = carBookingTime;
    }

    public String getCarBookingTime() {
        return mCarBookingTime;
    }

    public void setCarBookingDuration(int carBookingDuration) {
        mCarBookingDuration = carBookingDuration;
    }

    public int getCarBookingDuration() {
        return mCarBookingDuration;
    }

    @Override
    public int compareTo(Car car) {
        return mCarName.compareToIgnoreCase(car.getCarName());
    }
}
