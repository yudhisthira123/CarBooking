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
 * Created by yudhisthira on 16/05/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String     DATABASE_NAME  = "car_booking.db";
    private static final int        SCHEMA_VERSION = 1;
    private static DatabaseHelper   singleton      = null;
    private Context                 mContext       = null;

    public interface IDatabaseListener {
        public void onSuccessOperation(int count);
        public void onSuccessList(List<Car> carList);
        public void onFailure();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        mContext = context;
    }

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

            db.execSQL("CREATE TABLE booked_content (car_name TEXT," + "car_id INTEGER," + "car_description TEXT,"
                    + "car_short_description TEXT," + "car_booking_date TEXT," + "car_booking_time TEXT PRIMARY KEY NOT NULL,"
                    + "car_booking_duration INTEGER," + "car_image_url TEXT);");

            db.setTransactionSuccessful();
        }
        catch (Exception e) {

        }
        finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *
     * @param car
     */
    public void addBookingAsync(Car car, IDatabaseListener listener) {
        AddBookingAsyncTask task = new AddBookingAsyncTask(car, listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            task.execute();
        }
    }

    private class AddBookingAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private Car car;
        private IDatabaseListener listener;

        public AddBookingAsyncTask(Car car, IDatabaseListener listener) {
            this.car = car;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean bResult = false;

            String[] args = {
                    car.getCarName(),
                    String.valueOf(car.getCarID()),
                    car.getCarDescription(),
                    car.getCarShortDescription(),
                    car.getCarBookingDate(),
                    car.getCarBookingTime(),
                    String.valueOf(car.getCarBookingDuration()),
                    car.getImagePath()
            };

            try {
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL(
                        "INSERT OR REPLACE INTO booked_content (car_name,"
                                + " car_id,"
                                + " car_description,"
                                + " car_short_description,"
                                + " car_booking_date,"
                                + " car_booking_time,"
                                + " car_booking_duration,"
                                + " car_image_url)"
                                + " VALUES (?,?, ?, ?, ?, ?, ?, ?)",
                        args);

                bResult = true;
            }
            catch (Exception e) {

            }

            return bResult;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                listener.onSuccessOperation(-1);
            }
            else {
                listener.onFailure();
            }
        }
    }

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

            }

            Collections.sort(carList);

            return carList;
        }
    }

    public void deleteBooking(String bookingTime, IDatabaseListener listener) {
        DeleteBookingAsyncTask task = new DeleteBookingAsyncTask(bookingTime, listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            task.execute();
        }
    }

    private class DeleteBookingAsyncTask extends AsyncTask<Void,Void, Boolean> {

        private String                  bookingTime;
        private IDatabaseListener       listener;

        public DeleteBookingAsyncTask(String bookingTime, IDatabaseListener listener) {
            this.bookingTime = bookingTime;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result = true;

            try {
                SQLiteDatabase db = getWritableDatabase();
                result = db.delete("booked_content", "car_booking_time=?", new String[]{this.bookingTime}) > 0;
            }
            catch (SQLiteException ex) {
                result = false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(true == result) {
                listener.onSuccessOperation(-1);
            }
            else {
                listener.onFailure();
            }
        }
    }
}
