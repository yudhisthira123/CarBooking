package com.example.yudhisthira.carbooking.model;

import android.content.Context;

import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.database.DatabaseHelper;
import com.example.yudhisthira.carbooking.database.DatabaseWrapper;

import java.util.List;

/**
 * Created by yudhisthira on 16/05/17.
 */

public class BookedCarListModelImpl implements ICarListModel, DatabaseHelper.IDatabaseListener{

    private List<Car> mCarList = null;

    /**
     * Car list response callback object
     */
    private  ICarsResponseCallback mCallback;

    @Override
    public void fetchCarsData(Context context, ICarsResponseCallback callback) {
        mCallback = callback;
        DatabaseWrapper.getAllBookingAsync(context, this);
    }

    @Override
    public void getCarDetails(String carID, ICarsResponseCallback callback) {

    }

    @Override
    public List<Car> getStoredData() {
        return null;
    }

    @Override
    public void onSuccessOperation(int count) {

    }

    @Override
    public void onSuccessList(List<Car> carList) {
        mCarList = carList;
        if(null != mCallback) {
            mCallback.onSuccess(carList);
        }
    }

    @Override
    public void onFailure() {

    }
}
