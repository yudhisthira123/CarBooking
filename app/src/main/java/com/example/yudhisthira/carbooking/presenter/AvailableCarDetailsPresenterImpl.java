package com.example.yudhisthira.carbooking.presenter;

import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.model.IAvailableCarDetailModel;
import com.example.yudhisthira.carbooking.view.IAvailableCarDetailView;

/**
 * Created by yudhisthira on 16/05/17.
 */

public class AvailableCarDetailsPresenterImpl implements IAvailableCarDetailsPresenter,
        IAvailableCarDetailModel.IAvailabeCarDetailCallback{

    private IAvailableCarDetailView mAvailableCarDetailView;
    private IAvailableCarDetailModel mAvailableCarDetailModel;

    public AvailableCarDetailsPresenterImpl(IAvailableCarDetailView availableCarDetailView, IAvailableCarDetailModel availableCarDetailModel) {
        mAvailableCarDetailView = availableCarDetailView;
        mAvailableCarDetailModel = availableCarDetailModel;
    }

    @Override
    public void fetchCarsData(String carID) {
        mAvailableCarDetailModel.fetchCarDetails(this, carID);
    }

    @Override
    public void onSuccess(Car car) {
        mAvailableCarDetailView.updateCarDetails(car);
    }

    @Override
    public void onFailure() {

    }
}
