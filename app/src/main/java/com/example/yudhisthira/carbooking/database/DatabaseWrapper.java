package com.example.yudhisthira.carbooking.database;

import android.content.Context;

import com.example.yudhisthira.carbooking.data.Car;

/**
 * Created by yudhisthira on 16/05/17.
 */

public class DatabaseWrapper {

    public static final class CarColumns {
        public static final String CAR_NAME                 = "car_name";
        public static final String CAR_ID                   = "car_id";
        public static final String CAR_DESCRIPTION          = "car_description";
        public static final String CAR_SHORT_DESCRIPTION    = "car_short_description";
        public static final String CAR_IMAGE_URL            = "car_image_url";
        public static final String CAR_BOOKING_DATE         = "car_booking_date";
        public static final String CAR_BOOKING_TIME         = "car_booking_time";
        public static final String CAR_BOOKING_DURATION     = "car_booking_duration";
    }

    private static final String CONTENT_TABLE = "booked_content";
    private static final String CAR_IMAGE_BASE_URL = "http://sebastianf.net/intive/api/";

    public static void addBookingAsync(Context context, Car car, DatabaseHelper.IDatabaseListener listener) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.addBookingAsync(car, listener);
    }

    public static void getAllBookingAsync(Context context, DatabaseHelper.IDatabaseListener listener) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.getAllBookingAsync(listener);
    }

    public static void deleteBooking(Context context, String bookingTime, DatabaseHelper.IDatabaseListener listener) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.deleteBooking(bookingTime, listener);
    }

}
