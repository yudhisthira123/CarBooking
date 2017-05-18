package com.example.yudhisthira.carbooking.presenter;

import android.content.Context;

import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.model.ICarListModel;
import com.example.yudhisthira.carbooking.view.IAvailableCarListView;

import java.util.List;

/**
 *
 * This is implementation of ICarListPresenter
 */
public class CarListPresenterImpl implements ICarListPresenter, ICarListModel.ICarsResponseCallback {

    /**
     * IMainView instance
     */
    private IAvailableCarListView mMainView;

    /**
     * ICarListModel instance
     */
    private ICarListModel mMainModel;

    /**
     * Constructor of this class
     * @param mainView IMainView instance
     * @param mainModel ICarListModel instance
     */
    public CarListPresenterImpl(IAvailableCarListView mainView, ICarListModel mainModel) {
        mMainView = mainView;
        mMainModel = mainModel;
    }

    /**
     * This function sends asynchronous request to backend to fetch car list
     */
    @Override
    public void fetchCarsData(Context context) {
        mMainView.showProgress();

        mMainModel.fetchCarsData(context, this);
    }

    /**
     *
     * @return List of Car
     *
     * This function returns the already stored information
     */
    @Override
    public List<Car> getStoredData() {
        return mMainModel.getStoredData();
    }

    /**
     * @param carList List of car
     *
     * This function notify success response with list of car
     */
    @Override
    public void onSuccess(List<Car> carList) {
        mMainView.showCars(carList);
    }

    /**
     * This function notify failure response
     */
    @Override
    public void onFailure() {
        mMainView.showErrorMessage();
    }
}
