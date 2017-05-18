package com.example.yudhisthira.carbooking.presenter;

/**
 * Created by yudhisthira on 17/05/17.
 */

public interface IAvailableCarDetailsPresenter {
    /**
     * This function sends asynchronous request to backend to fetch car list
     */
    public void fetchCarsData(String carID);
}
