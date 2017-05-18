package com.example.yudhisthira.carbooking.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * The type Car.
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

    private String              mCarBookingID;

    /**
     * Gets car name.
     *
     * @return the car name
     */
    public String getCarName() {
        return mCarName;
    }

    /**
     * Sets car name.
     *
     * @param carName the car name
     */
    public void setCarName(String carName) {
        mCarName = carName;
    }

    /**
     * Gets car short description.
     *
     * @return the car short description
     */
    public String getCarShortDescription() {
        return mShortDescription;
    }

    /**
     * Sets car short description.
     *
     * @param carDescription the car description
     */
    public void setCarShortDescription(String carDescription) {
        mShortDescription = carDescription;
    }

    /**
     * Gets car description.
     *
     * @return the car description
     */
    public String getCarDescription() {
        return mCarDescription;
    }

    /**
     * Sets car description.
     *
     * @param carDescription the car description
     */
    public void setCarDescription(String carDescription) {
        mCarDescription = carDescription;
    }

    /**
     * Gets car id.
     *
     * @return the car id
     */
    public int getCarID() {
        return mCarID;
    }

    /**
     * Sets car id.
     *
     * @param carID the car id
     */
    public void setCarID(int carID) {
        mCarID = carID;
    }

    /**
     * Gets image path.
     *
     * @return the image path
     */
    public String getImagePath() {
        return mCarImage;
    }

    /**
     * Sets image path.
     *
     * @param carImage the car image
     */
    public void setImagePath(String carImage) {
        mCarImage = carImage;
    }

    /**
     * Sets car booking date.
     *
     * @param carBookingDate the car booking date
     */
    public void setCarBookingDate(String carBookingDate) {
        mCarBookingDate = carBookingDate;
    }

    /**
     * Gets car booking date.
     *
     * @return the car booking date
     */
    public String getCarBookingDate() {
        return mCarBookingDate;
    }

    /**
     * Sets car booking time.
     *
     * @param carBookingTime the car booking time
     */
    public void setCarBookingTime(String carBookingTime) {
        mCarBookingTime = carBookingTime;
    }

    /**
     * Gets car booking time.
     *
     * @return the car booking time
     */
    public String getCarBookingTime() {
        return mCarBookingTime;
    }

    /**
     * Sets car booking duration.
     *
     * @param carBookingDuration the car booking duration
     */
    public void setCarBookingDuration(int carBookingDuration) {
        mCarBookingDuration = carBookingDuration;
    }

    /**
     * Gets car booking duration.
     *
     * @return the car booking duration
     */
    public int getCarBookingDuration() {
        return mCarBookingDuration;
    }

    /**
     * Sets car booking id.
     *
     * @param bookingID the booking id
     */
    public void setCarBookingID(String bookingID) {
        mCarBookingID = bookingID;
    }

    /**
     * Gets car booking id.
     *
     * @return the car booking id
     */
    public String getCarBookingID() {
        return mCarBookingID;
    }

    @Override
    public int compareTo(Car car) {
        return mCarName.compareToIgnoreCase(car.getCarName());
    }
}
