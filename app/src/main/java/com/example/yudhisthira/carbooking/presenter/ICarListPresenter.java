package com.example.yudhisthira.carbooking.presenter;


import android.content.Context;

import com.example.yudhisthira.carbooking.data.Car;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This Car list presenter interface
 */
public interface ICarListPresenter {
    /**
     * This function sends asynchronous request to backend to fetch car list
     */
    public void fetchCarsData(Context context);

    /**
     *
     * @return List of Car
     *
     * This function returns the already stored information
     */
    public List<Car> getStoredData();
}
