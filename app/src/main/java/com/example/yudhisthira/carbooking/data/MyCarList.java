package com.example.yudhisthira.carbooking.data;

import java.util.List;

/**
 * Created by yudhisthira on 12/05/17.
 *
 * This class holds the list of Car response. GSON will serialize response into this
 */
public class MyCarList {
    private List<Car>  carList;

    /**
     *
     * @return List of Car objects
     */
    public List<Car> getCarList() {
        return carList;
    }
}
