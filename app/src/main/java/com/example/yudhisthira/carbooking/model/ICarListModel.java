package com.example.yudhisthira.carbooking.model;


import android.content.Context;

import com.example.yudhisthira.carbooking.data.Car;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This is Car list model interface
 */
public interface ICarListModel {

    /**
     * This is callback interface. It has to implement by caller.
     *
     * Succes and failure esponse will be notify here.
     */
    public interface ICarsResponseCallback{

        /**
         * @param carList List of car
         *
         * This function notify success response with list of car
         */
        public void onSuccess(List<Car> carList);

        /**
         * This function notify failure response
         */
        public void onFailure();
    }

    /**
     *
     * @param callback ICarsResponseCallback object where response will be notify
     */
    public void fetchCarsData(Context context, ICarsResponseCallback callback);

    /**
     *
     * @param callback ICarsResponseCallback object where response will be notify
     */
    public void getCarDetails(String carID, ICarsResponseCallback callback) ;

    /**
     *
     * @return List of Car objects.
     *
     * This function will return stored List of Car
     */
    public List<Car> getStoredData();
}
