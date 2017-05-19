package com.example.yudhisthira.carbooking.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yudhisthira.carbooking.Adapter.IMainActivityInterface;
import com.example.yudhisthira.carbooking.activity.BookingReceiver;
import com.example.yudhisthira.carbooking.activity.R;
import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.data.CommonConstants;
import com.example.yudhisthira.carbooking.database.DatabaseErrorCodes;
import com.example.yudhisthira.carbooking.database.DatabaseHelper;
import com.example.yudhisthira.carbooking.database.DatabaseWrapper;
import com.example.yudhisthira.carbooking.model.AvailableCarDetailModelImpl;
import com.example.yudhisthira.carbooking.presenter.AvailableCarDetailsPresenterImpl;
import com.example.yudhisthira.carbooking.presenter.IAvailableCarDetailsPresenter;
import com.example.yudhisthira.carbooking.view.IAvailableCarDetailView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by yudhisthira on 17/05/17.
 */
public class CarDetailsFragment extends Fragment
        implements IAvailableCarDetailView,
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        DatabaseHelper.IDatabaseListener,
        TextWatcher{

    private int             mCardID;
    private ImageView       mImageView;
    private TextView        mCarNameView1;
    private EditText        mCarDaysView1;

    private Button          mCarDateBtn;
    private Button          mCarTimeBtn;
    private TextView        mCarDateView1;
    private TextView        mCarTimeView1;

    private Button          mCarBookBtn;
    private CheckBox        mCustomBookingCheck;

    private int             mYear, mMonth, mDay, mHour, mMinute;

    private Car             mCarInfo;

    private String          mUniqueID;

    private IMainActivityInterface mMainActivityInterface;

    /**
     * New instance car details fragment.
     *
     * @return the car details fragment
     */
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

        mMainActivityInterface = (IMainActivityInterface) getActivity();

        return v;
    }

    @Override
    public void updateCarDetails(Car car) {

        mCarInfo = car;

        mCarInfo.setCarBookingDate(mCarDateView1.getText().toString());
        mCarInfo.setCarBookingTime(mCarTimeView1.getText().toString());

        mCarNameView1.setText(car.getCarName());

        mMainActivityInterface.updateTitle(car.getCarName());

        StringBuffer string = new StringBuffer("http://sebastianf.net/intive/api/");
        string.append(car.getImagePath());

        Picasso.with(getContext())
                .load(string.toString())
                .error(R.drawable.default_image_icon)
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
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog tp = new TimePickerDialog(getContext(), this, mHour, mMinute, true);
            tp.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        if(R.id.custom_booking_check ==  id) {
            enableDateAndTimeControls(isChecked);
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

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(c.getTime());
        mCarTimeView1.setText(time);

        mCarInfo.setCarBookingTime(time);
    }

    @Override
    public void onSuccessOperation(int count) {
        Log.d("", "");

        setAlarm();

        getFragmentManager().popBackStack();

        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessList(List<Car> carList) {
        Log.d("", "");
    }

    @Override
    public void onFailure(int errorCode) {

        String message = "Request Failed";

        switch (errorCode) {
            case DatabaseErrorCodes.DATABASE_FAILURE:
                message = "Could not book car. Try after some time";
                break;

            case DatabaseErrorCodes.DATABASE_DUPLICATE:
                message = "This Car is already booked for this date and time";
                break;

            default:
                break;

        }

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("","");

        String number = s.toString();

        if(s.length() > 0) {
            int num = Integer.parseInt(number);
            if (num >= 1 && num <= 7) {
                //mCarDaysView1.setText("" + num);
            }
            else{
                Toast.makeText(getActivity(), " Please enter the number in the range of 1-7", Toast.LENGTH_SHORT).show();
                mCarDaysView1.setText("1");
            }
        }
    }

    private void bookCar() {
        String strValue = mCarDaysView1.getText().toString();
        mCarInfo.setCarBookingDuration(Integer.parseInt(strValue));
        mUniqueID = UUID.randomUUID().toString();

        DatabaseWrapper.addBookingAsync(getContext(), mCarInfo, mUniqueID, this);
    }

    private void setUpViews(View v) {
        mImageView = (ImageView)v.findViewById(R.id.available_car_image);
        mCarNameView1 = (TextView) v.findViewById(R.id.available_car_name1);
        mCarDaysView1 = (EditText) v.findViewById(R.id.available_car_days1);
        mCarDaysView1.addTextChangedListener(this);

        mCarDateBtn = (Button) v.findViewById(R.id.available_car_date);
        mCarDateBtn.setOnClickListener(this);
        mCarDateView1 = (TextView) v.findViewById(R.id.available_car_date1);

        mCarTimeBtn = (Button) v.findViewById(R.id.available_car_time);
        mCarTimeBtn.setOnClickListener(this);
        mCarTimeView1 = (TextView) v.findViewById(R.id.available_car_time1);

        mCarBookBtn = (Button) v.findViewById(R.id.btnBookCar);
        mCarBookBtn.setOnClickListener(this);

        mCustomBookingCheck = (CheckBox) v.findViewById(R.id.custom_booking_check);
        mCustomBookingCheck.setOnCheckedChangeListener(this);

        setDefaultDateAndTime();
    }

    private void setDefaultDateAndTime() {
        Calendar c = Calendar.getInstance();

        //set Next date always
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());
        mCarDateView1.setText(date);

        df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(c.getTime());
        mCarTimeView1.setText(time);

    }

    private void setAlarm() {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sd.parse(mCarInfo.getCarBookingDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = sd.getCalendar();

        Calendar calendar = getTimeCalender();

        c.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, calendar.get(Calendar.SECOND));

        final int _id = (int) System.currentTimeMillis();

        Intent intent = new Intent(getContext(), BookingReceiver.class);
        //intent.setAction(mCarInfo.getCarBookingTime());
        intent.setAction(mCarInfo.getCarBookingID());
        intent.putExtra(CommonConstants.CAR_NAME, mCarInfo.getCarName());
        intent.putExtra(CommonConstants.CAR_BOOKING_TIME, mCarInfo.getCarBookingTime());
        //intent.putExtra(CommonConstants.CAR_BOOKING_ID, mCarInfo.getCarBookingID());
        intent.putExtra(CommonConstants.CAR_BOOKING_ID, mUniqueID);
        intent.putExtra(CommonConstants.NOTIFICATION_ID, _id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private Calendar getTimeCalender() {
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        try {
            sd.parse(mCarInfo.getCarBookingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = sd.getCalendar();

        return c;
    }

    private void enableDateAndTimeControls(boolean bEnable) {
        mCarDateBtn.setEnabled(bEnable);
        mCarTimeBtn.setEnabled(bEnable);
        mCarDaysView1.setEnabled(bEnable);
    }
}
