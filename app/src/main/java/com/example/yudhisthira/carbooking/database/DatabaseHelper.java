package com.example.yudhisthira.carbooking.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;

import com.example.yudhisthira.carbooking.data.Car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yudhisthira on 17/05/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String     DATABASE_NAME           = "car_booking.db";
    private static final int        SCHEMA_VERSION          = 1;
    private static DatabaseHelper   singleton               = null;
    private Context                 mContext                = null;

    /**
     * The interface Database listener.
     */
    public interface IDatabaseListener {
        /**
         * On success operation.
         *
         * @param count the count
         */
        public void onSuccessOperation(int count);

        /**
         * On success list.
         *
         * @param carList the car list
         */
        public void onSuccessList(List<Car> carList);

        /**
         * On failure.
         */
        public void onFailure(int errorCode);
    }

    /**
     * Instantiates a new Database helper.
     *
     * @param context the context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        mContext = context;
    }

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if(null == singleton) {
            singleton = new DatabaseHelper(context);
        }

        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.beginTransaction();

//            db.execSQL("CREATE TABLE booked_content (car_name TEXT," + "car_id INTEGER PRIMARY KEY NOT NULL," + "car_description TEXT,"
//                    + "car_short_description TEXT," + "car_booking_date TEXT," + "car_booking_time TEXT PRIMARY KEY NOT NULL,"
//                    + "car_booking_duration INTEGER," + "car_image_url TEXT);");

            db.execSQL("CREATE TABLE booked_content ( car_booking_id TEXT PRIMARY KEY NOT NULL," + "car_name TEXT," + "car_id INTEGER," + "car_description TEXT,"
                    + "car_short_description TEXT," + "car_booking_date TEXT," + "car_booking_time TEXT,"
                    + "car_booking_duration INTEGER," + "car_image_url TEXT," + "car_backend_sync_status INTEGER);");

            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Add booking async.
     *
     * @param car       the car
     * @param bookingID the booking id
     * @param listener  the listener
     */
    public void addBookingAsync(Car car, String bookingID, IDatabaseListener listener) {
        AddBookingAsyncTask task = new AddBookingAsyncTask(car, bookingID, listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            task.execute();
        }
    }

    private class AddBookingAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private Car                     car;
        private String                  bookingID;
        private IDatabaseListener       listener;
        private boolean                 isDuplicate;

        /**
         * Instantiates a new Add booking async task.
         *
         * @param car       the car
         * @param bookingID the booking id
         * @param listener  the listener
         */
        public AddBookingAsyncTask(Car car, String bookingID, IDatabaseListener listener) {
            this.bookingID = bookingID;
            this.car = car;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean bResult = false;

            String[] args = {
                    bookingID,
                    car.getCarName(),
                    String.valueOf(car.getCarID()),
                    car.getCarDescription(),
                    car.getCarShortDescription(),
                    car.getCarBookingDate(),
                    car.getCarBookingTime(),
                    String.valueOf(car.getCarBookingDuration()),
                    car.getImagePath(),
                    String.valueOf(BackendSyncState.SYNC_STATE_PENDING)
            };

            try {

                isDuplicate = isDuplicateBoooking(car);

                if(false == isDuplicate) {
                    SQLiteDatabase db = getWritableDatabase();
                    db.execSQL(
                            "INSERT OR REPLACE INTO booked_content (car_booking_id,"
                                    + " car_name,"
                                    + " car_id,"
                                    + " car_description,"
                                    + " car_short_description,"
                                    + " car_booking_date,"
                                    + " car_booking_time,"
                                    + " car_booking_duration,"
                                    + " car_image_url,"
                                    + " car_backend_sync_status)"
                                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            args);

                    bResult = true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return bResult;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                listener.onSuccessOperation(-1);
            }
            else {
                if(true == isDuplicate) {
                    listener.onFailure(DatabaseErrorCodes.DATABASE_DUPLICATE);
                }
                else {
                    listener.onFailure(DatabaseErrorCodes.DATABASE_FAILURE);
                }
            }
        }

        private boolean isDuplicateBoooking(Car car) {
            String query = "SELECT * FROM booked_content WHERE car_booking_time= \"" + car.getCarBookingTime() +"\"" + "AND" + " car_id=" + car.getCarID();

            //select * from booked_content where car_booking_time="ddd" AND car_id=12;

            Cursor cursor = getReadableDatabase().rawQuery(query, null);

            int count = cursor.getCount();

            return (count > 0);
        }
    }

    /**
     * Gets all booking async.
     *
     * @param listener the listener
     */
    public void getAllBookingAsync(IDatabaseListener listener) {
        GetAllBookingAsyncTask task = new GetAllBookingAsyncTask(listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            task.execute();
        }
    }

    private class GetAllBookingAsyncTask extends AsyncTask<Void, Void, List<Car>> {

        private Cursor cursor = null;
        private IDatabaseListener listener;

        /**
         * Instantiates a new Get all booking async task.
         *
         * @param listener the listener
         */
        public GetAllBookingAsyncTask(IDatabaseListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Car> doInBackground(Void... params) {

            cursor = getReadableDatabase().rawQuery("SELECT * FROM booked_content", null);

            return parseCursor(cursor);
        }

        @Override
        protected void onPostExecute(List<Car> carList) {
            listener.onSuccessList(carList);
        }

        private List<Car> parseCursor(Cursor cursor) {
            List<Car> carList = new ArrayList<>();
            try {
                cursor.moveToFirst();

                do {
                    Car car = new Car();

                    car.setCarBookingID(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_ID)));
                    car.setCarName(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_NAME)));
                    car.setCarDescription(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_DESCRIPTION)));
                    car.setCarShortDescription(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_SHORT_DESCRIPTION)));
                    car.setCarID(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_ID)));
                    car.setCarBookingDate(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_DATE)));
                    car.setCarBookingTime(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_TIME)));
                    car.setCarBookingDuration(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_DURATION)));
                    car.setImagePath(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_IMAGE_URL)));

                    carList.add(car);

                }while (cursor.moveToNext() && cursor.getCount() > 0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            Collections.sort(carList);

            return carList;
        }
    }

    /**
     * Gets booking async.
     *
     * @param bookingID the booking id
     * @param listener  the listener
     */
    public void getBookingAsync(String bookingID, IDatabaseListener listener) {
        GetBookingAsyncTask task = new GetBookingAsyncTask(bookingID, listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            task.execute();
        }
    }

    private class GetBookingAsyncTask extends AsyncTask<Void, Void, List<Car>> {

        private Cursor                  cursor = null;
        private String                  bookingID;
        private IDatabaseListener       listener;

        /**
         * Instantiates a new Get booking async task.
         *
         * @param bookingID the booking id
         * @param listener  the listener
         */
        public GetBookingAsyncTask(String bookingID, IDatabaseListener listener) {
            this.bookingID = bookingID;
            this.listener = listener;
        }

        @Override
        protected List<Car> doInBackground(Void... params) {

            String query = "SELECT * FROM booked_content WHERE car_booking_id= \"" + bookingID +"\"";

            cursor = getReadableDatabase().rawQuery(query, null);


            return parseCursor(cursor);
        }

        @Override
        protected void onPostExecute(List<Car> carList) {
            if (null != carList && carList.size() > 0) {
                listener.onSuccessList(carList);
            }
            else {
                listener.onFailure(DatabaseErrorCodes.DATABASE_NO_ENTRY);
            }
        }

        private List<Car> parseCursor(Cursor cursor) {
            List<Car> carList = new ArrayList<>();
            try {
                cursor.moveToFirst();

                do {
                    Car car = new Car();

                    car.setCarBookingID(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_ID)));
                    car.setCarName(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_NAME)));
                    car.setCarDescription(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_DESCRIPTION)));
                    car.setCarShortDescription(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_SHORT_DESCRIPTION)));
                    car.setCarID(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_ID)));
                    car.setCarBookingDate(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_DATE)));
                    car.setCarBookingTime(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_TIME)));
                    car.setCarBookingDuration(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_DURATION)));
                    car.setImagePath(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_IMAGE_URL)));

                    carList.add(car);

                }while (cursor.moveToNext() && cursor.getCount() > 0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            Collections.sort(carList);

            return carList;
        }
    }

    /**
     * Delete booking.
     *
     * @param bookingID the booking id
     * @param listener  the listener
     */
    public void deleteBooking(String bookingID, IDatabaseListener listener) {
        DeleteBookingAsyncTask task = new DeleteBookingAsyncTask(bookingID, listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            task.execute();
        }
    }

    private class DeleteBookingAsyncTask extends AsyncTask<Void,Void, Boolean> {

        private String                  bookingID;
        private IDatabaseListener       listener;

        /**
         * Instantiates a new Delete booking async task.
         *
         * @param bookingID the booking id
         * @param listener  the listener
         */
        public DeleteBookingAsyncTask(String bookingID, IDatabaseListener listener) {
            this.bookingID = bookingID;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result = true;

            try {
                SQLiteDatabase db = getWritableDatabase();
                result = db.delete("booked_content", "car_booking_id=?", new String[]{this.bookingID}) > 0;
            }
            catch (SQLiteException ex) {
                result = false;

                ex.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(true == result) {
                listener.onSuccessOperation(-1);
            }
            else {
                listener.onFailure(DatabaseErrorCodes.DATABASE_FAILURE);
            }
        }
    }

    /**
     * Gets pending sync events.
     *
     * @return the pending sync events
     */
    public List<Car> getPendingSyncEvents() {
        List<Car> carList = new ArrayList<>();

        try {
            String query = "SELECT * FROM booked_content WHERE car_backend_sync_status= \"" + BackendSyncState.SYNC_STATE_PENDING +"\"";

            Cursor cursor = getReadableDatabase().rawQuery(query, null);

            cursor.moveToFirst();

            do {
                Car car = new Car();

                car.setCarBookingID(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_ID)));
                car.setCarName(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_NAME)));
                car.setCarDescription(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_DESCRIPTION)));
                car.setCarShortDescription(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_SHORT_DESCRIPTION)));
                car.setCarID(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_ID)));
                car.setCarBookingDate(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_DATE)));
                car.setCarBookingTime(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_TIME)));
                car.setCarBookingDuration(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_BOOKING_DURATION)));
                car.setImagePath(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.CarColumns.CAR_IMAGE_URL)));

                carList.add(car);

            }while (cursor.moveToNext() && cursor.getCount() > 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return carList;
    }

    /**
     * Update backend sync status.
     *
     * @param car        the car
     * @param syncStatus the sync status
     */
    public void updateBackendSyncStatus(Car car, int syncStatus) {

        SQLiteDatabase db = getWritableDatabase();

        try {

            String query = "UPDATE booked_content SET car_backend_sync_status = \"" + syncStatus + "\" WHERE car_booking_id= \"" + car.getCarBookingID() +"\"";

            db.execSQL(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }
}
