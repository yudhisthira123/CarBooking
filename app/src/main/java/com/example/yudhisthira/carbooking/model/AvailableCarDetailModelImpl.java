package com.example.yudhisthira.carbooking.model;

import com.example.yudhisthira.carbooking.data.Car;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yudhisthira on 16/05/17.
 */

public class AvailableCarDetailModelImpl implements IAvailableCarDetailModel {
    private IAvailabeCarDetailCallback mCallBack;


    @Override
    public void fetchCarDetails(IAvailabeCarDetailCallback callback, String carID) {
        mCallBack = callback;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://www.sebastianf.net/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MyCarListClient myCarListClient = retrofit.create(MyCarListClient.class);

        Call<Car> call = myCarListClient.getCarDetails(carID);

        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

                Car car = (Car) response.body();

                mCallBack.onSuccess(car);
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                mCallBack.onFailure();
            }
        });
    }
}
