package com.example.yudhisthira.carbooking.view;

import com.example.yudhisthira.carbooking.data.Car;

import java.util.List;

/**
 * Created by yudhisthira on 16/05/17.
 */

public interface IAvailableCarListView {
    /**
     * Show progress wheel on UI while loading car list data
     */
    void showProgress();

    /**
     * @param carList List of Car object.
     *
     * Display car list on UI
     */
    void showCars(List<Car> carList);

    /**
     * Show error message if Any.
     */
    void showErrorMessage();
}
