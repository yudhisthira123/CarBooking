package com.example.yudhisthira.carbooking.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yudhisthira.carbooking.activity.R;
import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.data.CommonConstants;
import com.example.yudhisthira.carbooking.database.DatabaseHelper;
import com.example.yudhisthira.carbooking.database.DatabaseWrapper;
import com.example.yudhisthira.carbooking.model.AvailableCarDetailModelImpl;
import com.example.yudhisthira.carbooking.presenter.AvailableCarDetailsPresenterImpl;
import com.example.yudhisthira.carbooking.presenter.IAvailableCarDetailsPresenter;
import com.example.yudhisthira.carbooking.view.IAvailableCarDetailView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yudhisthira on 16/05/17.
 */

public class CarDetailsFragment extends Fragment
        implements IAvailableCarDetailView,
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        DatabaseHelper.IDatabaseListener{

    private int             mCardID;
    private ImageView       mImageView;
    private TextView        mCarNameView1;
    private TextView        mCarDaysView1;

    private Button          mCarDateBtn;
    private Button          mCarTimeBtn;
    private TextView        mCarDateView1;
    private TextView        mCarTimeView1;

    private Button          mCarBookBtn;

    private int             mYear, mMonth, mDay, mHour, mMinute;

    private Car             mCarInfo;

    public static CarDetailsFragment newInstance() {
        return new CarDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle b = getArguments();
        mCardID = b.getInt(CommonConstants.CAR_ID);

        View v = inflater.inflate(R.layout.car_details_view, container, false);

        setUpViews(v);

        IAvailableCarDetailsPresenter presenter = new AvailableCarDetailsPresenterImpl(this, new AvailableCarDetailModelImpl());
        presenter.fetchCarsData("" + mCardID);

        return v;
    }

    @Override
    public void updateCarDetails(Car car) {

        mCarInfo = car;

        mCarInfo.setCarBookingDate(mCarDateView1.getText().toString());
        mCarInfo.setCarBookingTime(mCarTimeView1.getText().toString());

        mCarNameView1.setText(car.getCarName());

        StringBuffer string = new StringBuffer("http://sebastianf.net/intive/api/");
        string.append(car.getImagePath());

        Picasso.with(getContext())
                .load(string.toString())
                .into(mImageView);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnBookCar) {
            bookCar();
        }
        else if(id == R.id.available_car_date) {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dp = new DatePickerDialog(getContext(), this, mYear, mMonth, mDay);
            dp.show();

        }

        else if(R.id.available_car_time == id) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog tp = new TimePickerDialog(getContext(), this, mHour, mMinute, true);
            tp.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());
        mCarDateView1.setText(date);

        mCarInfo.setCarBookingDate(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(c.getTime());
        mCarTimeView1.setText(time);

        mCarInfo.setCarBookingTime(time);
    }

    @Override
    public void onSuccessOperation(int count) {
        Log.d("", "");

        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessList(List<Car> carList) {
        Log.d("", "");
    }

    @Override
    public void onFailure() {

    }

    private void bookCar() {
        String strValue = mCarDaysView1.getText().toString();
        mCarInfo.setCarBookingDuration(Integer.parseInt(strValue));
        DatabaseWrapper.addBookingAsync(getContext(), mCarInfo, this);
    }

    private void setUpViews(View v) {
        mImageView = (ImageView)v.findViewById(R.id.available_car_image);
        mCarNameView1 = (TextView) v.findViewById(R.id.available_car_name1);
        mCarDaysView1 = (TextView) v.findViewById(R.id.available_car_days1);

        mCarDateBtn = (Button) v.findViewById(R.id.available_car_date);
        mCarDateBtn.setOnClickListener(this);
        mCarDateView1 = (TextView) v.findViewById(R.id.available_car_date1);

        mCarTimeBtn = (Button) v.findViewById(R.id.available_car_time);
        mCarTimeBtn.setOnClickListener(this);
        mCarTimeView1 = (TextView) v.findViewById(R.id.available_car_time1);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());
        mCarDateView1.setText(date);

        df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(c.getTime());
        mCarTimeView1.setText(time);

        mCarBookBtn = (Button) v.findViewById(R.id.btnBookCar);
        mCarBookBtn.setOnClickListener(this);
    }
}
