package com.example.yudhisthira.carbooking.model;

import com.example.yudhisthira.carbooking.data.Car;

/**
 * Created by yudhisthira on 16/05/17.
 */

public interface IAvailableCarDetailModel {

    public interface IAvailabeCarDetailCallback{
        /**
         * @param car car
         *
         * This function notify success response with list of car
         */
        public void onSuccess(Car car);

        /**
         * This function notify failure response
         */
        public void onFailure();
    }

    public void fetchCarDetails(IAvailabeCarDetailCallback callback, String carID);

}
