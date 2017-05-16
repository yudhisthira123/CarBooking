package com.example.yudhisthira.carbooking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yudhisthira.carbooking.Adapter.CarListAdapter;
import com.example.yudhisthira.carbooking.Adapter.IMainActivityInterface;
import com.example.yudhisthira.carbooking.activity.R;
import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.data.CommonConstants;
import com.example.yudhisthira.carbooking.model.CarListModelImpl;
import com.example.yudhisthira.carbooking.presenter.CarListPresenterImpl;
import com.example.yudhisthira.carbooking.presenter.ICarListPresenter;
import com.example.yudhisthira.carbooking.view.IAvailableCarListView;

import java.util.List;

/**
 * Created by yudhisthira on 15/05/17.
 */

public class CarListFragment extends Fragment
        implements IAvailableCarListView,
        CarListAdapter.CarItemListener,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String             TAG = CarListFragment.class.getSimpleName();

    private IMainActivityInterface          mMainActivityInterface;

    /**
     * Reference of Recycler view
     */
    private RecyclerView                    mRecyclerView;

    /**
     * Reference of Recycler adapter
     */
    private CarListAdapter                  mRecyclerAdapter;

    /**
     * Layout manager for recycler view
     */
    private RecyclerView.LayoutManager      mLayoutManager;

    /**
     * reference to ProgressBar
     */
    private ProgressBar                     mProgressBar;

    /**
     *  Reference to error text view
     */
    private TextView                        mErrorTextView;

    /**
     * Car list presenter interface
     */
    private ICarListPresenter               mMainPresenter;

    /**
     * List of Car object
     */
    private List<Car> mCarList;

    private SwipeRefreshLayout              mSwipeContainer;

    public static CarListFragment newInstance() {
        return new CarListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.available_car_list_view, container, false);

        mSwipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.available_car_swipe_container);
        mSwipeContainer.setOnRefreshListener(this);

        mProgressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        mErrorTextView = (TextView)v.findViewById(R.id.errorTextView);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerAdapter = new CarListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mMainPresenter = new CarListPresenterImpl(this, new CarListModelImpl());
        mMainPresenter.fetchCarsData(getContext());

        mMainActivityInterface = (IMainActivityInterface) getActivity();

        return v;
    }

    @Override
    public void showProgress() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCars(List<Car> carList) {
        mCarList = carList;

        mRecyclerAdapter.setData(mCarList);

        mSwipeContainer.setRefreshing(false);

        hideProgress();
    }

    @Override
    public void showErrorMessage() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setText("Fail to load Data!!!");
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCarItemClick(Car car) {
        CarDetailsFragment fragment = CarDetailsFragment.newInstance();

        Bundle b = new Bundle();
        b.putInt(CommonConstants.CAR_ID, car.getCarID());
        fragment.setArguments(b);

        mMainActivityInterface.replaceFragment(fragment);
    }

    @Override
    public void onRefresh() {
        mMainPresenter.fetchCarsData(getContext());
    }

    /**
     * This function hide progress wheel
     */
    private void hideProgress() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setVisibility(View.GONE);
        }
    }
}
