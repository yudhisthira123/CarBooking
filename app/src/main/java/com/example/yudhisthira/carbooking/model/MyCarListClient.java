package com.example.yudhisthira.carbooking.model;


import com.example.yudhisthira.carbooking.data.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *
 * This car list client interface by which we will request to Retrofit
 */
public interface MyCarListClient {

    /**
     *
     * @return
     *
     * GET request to Retrofit to fetch response for server
     */
    @GET("/intive/api/v1/cars.json")
    Call<List<Car>> fetchCarList();

    /**
     *
     * @return
     *
     * GET request to Retrofit to fetch response for server
     */
    @GET("/intive/api/v1/cars/{id}.json")
    Call<Car> getCarDetails(@Path("id") String carID);
}
