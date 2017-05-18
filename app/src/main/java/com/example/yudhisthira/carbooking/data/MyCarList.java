package com.example.yudhisthira.carbooking.data;

import java.util.List;

/**
 * <p>
 * This class holds the list of Car response. GSON will serialize response into this
 */
public class MyCarList {
    private List<Car>  carList;

    /**
     * Gets car list.
     *
     * @return List of Car objects
     */
    public List<Car> getCarList() {
        return carList;
    }
}
