package com.example.yudhisthira.carbooking.database;

import android.content.Context;

import com.example.yudhisthira.carbooking.data.Car;

import java.util.List;

/**
 * Created by yudhisthira on 17/05/17.
 */
public class DatabaseWrapper {

    /**
     * The type Car columns.
     */
    public static final class CarColumns {
        /**
         * The constant CAR_BOOKING_ID.
         */
        public static final String CAR_BOOKING_ID                   = "car_booking_id";
        /**
         * The constant CAR_NAME.
         */
        public static final String CAR_NAME                         = "car_name";
        /**
         * The constant CAR_ID.
         */
        public static final String CAR_ID                           = "car_id";
        /**
         * The constant CAR_DESCRIPTION.
         */
        public static final String CAR_DESCRIPTION                  = "car_description";
        /**
         * The constant CAR_SHORT_DESCRIPTION.
         */
        public static final String CAR_SHORT_DESCRIPTION            = "car_short_description";
        /**
         * The constant CAR_IMAGE_URL.
         */
        public static final String CAR_IMAGE_URL                    = "car_image_url";
        /**
         * The constant CAR_BOOKING_DATE.
         */
        public static final String CAR_BOOKING_DATE                 = "car_booking_date";
        /**
         * The constant CAR_BOOKING_TIME.
         */
        public static final String CAR_BOOKING_TIME                 = "car_booking_time";
        /**
         * The constant CAR_BOOKING_DURATION.
         */
        public static final String CAR_BOOKING_DURATION             = "car_booking_duration";
        /**
         * The constant CAR_BACKEND_SYNC_STATUS.
         */
//
        public static final String CAR_BACKEND_SYNC_STATUS          = "car_backend_sync_status";
    }

    private static final String CONTENT_TABLE = "booked_content";
    private static final String CAR_IMAGE_BASE_URL = "http://sebastianf.net/intive/api/";

    /**
     * Add booking async.
     *
     * @param context   the context
     * @param car       the car
     * @param bookingID the booking id
     * @param listener  the listener
     */
    public static void addBookingAsync(Context context, Car car, String bookingID, DatabaseHelper.IDatabaseListener listener) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.addBookingAsync(car, bookingID, listener);
    }

    /**
     * Gets all booking async.
     *
     * @param context  the context
     * @param listener the listener
     */
    public static void getAllBookingAsync(Context context, DatabaseHelper.IDatabaseListener listener) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.getAllBookingAsync(listener);
    }

    /**
     * Gets booking async.
     *
     * @param context   the context
     * @param bookingID the booking id
     * @param listener  the listener
     */
    public static void getBookingAsync(Context context, String bookingID, DatabaseHelper.IDatabaseListener listener) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.getBookingAsync(bookingID, listener);
    }

    /**
     * Delete booking async.
     *
     * @param context   the context
     * @param bookingID the booking id
     * @param listener  the listener
     */
    public static void deleteBookingAsync(Context context, String bookingID, DatabaseHelper.IDatabaseListener listener) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.deleteBooking(bookingID, listener);
    }

    /**
     * Gets pending sync events.
     *
     * @param context the context
     * @return the pending sync events
     */
    public static List<Car> getPendingSyncEvents(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        return helper.getPendingSyncEvents();
    }

    /**
     * Update backend sync status.
     *
     * @param context    the context
     * @param car        the car
     * @param syncStatus the sync status
     */
    public static void updateBackendSyncStatus(Context context, Car car, int syncStatus) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        helper.updateBackendSyncStatus(car, syncStatus);
    }
}
