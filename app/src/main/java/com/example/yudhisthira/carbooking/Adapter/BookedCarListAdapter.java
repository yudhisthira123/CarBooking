package com.example.yudhisthira.carbooking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yudhisthira.carbooking.activity.R;
import com.example.yudhisthira.carbooking.data.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudhisthira on 17/05/17.
 */
public class BookedCarListAdapter extends RecyclerView.Adapter<BookedCarListAdapter.ItemHolder>{
    private List<Car> mCarList = new ArrayList<>();

    private final Context mContext;
    private final CarListAdapter.CarItemListener mListener;

    /**
     * Instantiates a new Booked car list adapter.
     *
     * @param context  the context
     * @param listener the listener
     */
    public BookedCarListAdapter(Context context, CarListAdapter.CarItemListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public BookedCarListAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_item, parent, false);
        return new BookedCarListAdapter.ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(BookedCarListAdapter.ItemHolder holder, int position) {
        Car car = mCarList.get(position);
        holder.mCarName.setText(car.getCarName());
        holder.mCarDescription.setText(car.getCarShortDescription());

        holder.mCarID.setText("" + car.getCarID());
    }

    @Override
    public int getItemCount() {
        int size = 0;

        if(null != mCarList) {
            size = mCarList.size();
        }

        return size;
    }

    /**
     * Sets data.
     *
     * @param carList the car list
     */
    public void setData(List<Car> carList) {
        mCarList = carList;
        notifyDataSetChanged();
    }

    /**
     * The type Item holder.
     */
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCarName;
        private TextView mCarDescription;
        private TextView mCarID;

        /**
         * Instantiates a new Item holder.
         *
         * @param v the v
         */
        public ItemHolder(View v) {
            super(v);

            mCarName = (TextView) v.findViewById(R.id.carName);
            mCarDescription = (TextView) v.findViewById(R.id.carDescription);
            mCarID = (TextView) v.findViewById(R.id.carId);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");

            int pos = getAdapterPosition();

            Car car = mCarList.get(pos);

            mListener.onCarItemClick(car);
        }
    }

    /**
     * The interface Car item listener.
     */
    public interface CarItemListener {
        /**
         * On car item click.
         *
         * @param car the car
         */
        void onCarItemClick(Car car);
    }
}
