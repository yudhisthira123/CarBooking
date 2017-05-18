package com.example.yudhisthira.carbooking.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yudhisthira.carbooking.activity.BookingReceiver;
import com.example.yudhisthira.carbooking.activity.R;
import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.data.CommonConstants;
import com.example.yudhisthira.carbooking.database.DatabaseHelper;
import com.example.yudhisthira.carbooking.database.DatabaseWrapper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yudhisthira on 17/05/17.
 */
public class BookedCarDetailsFragment extends Fragment
    implements View.OnClickListener,
        DatabaseHelper.IDatabaseListener{
    private Car mCarInfo;

    private int             mCardID;
    private ImageView       mImageView;
    private TextView        mCarNameView1;
    private TextView        mCarDaysView1;

    private TextView        mCarDateView1;
    private TextView        mCarTimeView1;

    private Button          mDeleteBookingBtn;

    /**
     * New instance booked car details fragment.
     *
     * @return the booked car details fragment
     */
    public static BookedCarDetailsFragment newInstance() {
        return new BookedCarDetailsFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.booked_car_details_view, container, false);

        Bundle b = getArguments();
        mCarInfo = (Car) b.getSerializable(CommonConstants.CAR_OBJECT);

        setUpViews(v);
        displayCarInfo();

        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(R.id.btnDeleteBooking == id) {
            deleteBooking();
        }
    }

    @Override
    public void onSuccessOperation(int count) {
        removeAlarm();
        getFragmentManager().popBackStack();
    }

    /**
     *
     * @param carList
     */
    @Override
    public void onSuccessList(List<Car> carList) {

    }

    /**
     *
     */
    @Override
    public void onFailure() {

    }

    /**
     *
     */
    private void displayCarInfo() {

        mCarNameView1.setText(mCarInfo.getCarName());
        mCarDateView1.setText(mCarInfo.getCarBookingDate());
        mCarTimeView1.setText(mCarInfo.getCarBookingTime());
        mCarDaysView1.setText( "" + mCarInfo.getCarBookingDuration());

        StringBuffer string = new StringBuffer("http://sebastianf.net/intive/api/");
        string.append(mCarInfo.getImagePath());

        Picasso.with(getContext())
                .load(string.toString())
                .error(R.drawable.default_image_icon)
                .into(mImageView);
    }

    /**
     *
     * @param v
     */
    private void setUpViews(View v) {
        mImageView = (ImageView)v.findViewById(R.id.available_car_image);
        mCarNameView1 = (TextView) v.findViewById(R.id.available_car_name1);
        mCarDaysView1 = (TextView) v.findViewById(R.id.available_car_days1);

        mCarDateView1 = (TextView) v.findViewById(R.id.available_car_date1);
        mCarTimeView1 = (TextView) v.findViewById(R.id.available_car_time1);


        mDeleteBookingBtn = (Button) v.findViewById(R.id.btnDeleteBooking);
        mDeleteBookingBtn.setOnClickListener(this);
    }

    /**
     *
     */
    private void deleteBooking() {
        DatabaseWrapper.deleteBookingAsync(getContext(), mCarInfo.getCarBookingID(), this);
    }

    /**
     *
     */
    private void removeAlarm() {
        Intent intent = new Intent(getContext(), BookingReceiver.class);
//        intent.setAction(mCarInfo.getCarBookingTime());
        intent.setAction(mCarInfo.getCarBookingID());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }
}
