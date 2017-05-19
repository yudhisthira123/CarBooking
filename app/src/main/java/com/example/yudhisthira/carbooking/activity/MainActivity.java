package com.example.yudhisthira.carbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yudhisthira.carbooking.Adapter.IMainActivityInterface;
import com.example.yudhisthira.carbooking.data.Car;
import com.example.yudhisthira.carbooking.data.CommonConstants;
import com.example.yudhisthira.carbooking.database.BackendSyncScheduler;
import com.example.yudhisthira.carbooking.database.DatabaseErrorCodes;
import com.example.yudhisthira.carbooking.database.DatabaseHelper;
import com.example.yudhisthira.carbooking.database.DatabaseWrapper;
import com.example.yudhisthira.carbooking.drawer.DrawerPresenterImpl;
import com.example.yudhisthira.carbooking.fragment.BookedCarDetailsFragment;

import java.util.List;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DrawerPresenterImpl.DrawerView,
        IMainActivityInterface,
        DatabaseHelper.IDatabaseListener{

    private Toolbar                     toolbar;
    private DrawerLayout                drawerLayout;
    private NavigationView              navigationView;
    private DrawerPresenterImpl         drawerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupViews();

        drawerPresenter = new DrawerPresenterImpl(this);
        navigationView.getMenu().performIdentifierAction(R.id.nav_available_cars, 0);

        Intent intent = getIntent();

        if(null != intent) {
            handleNewIntent(intent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        BackendSyncScheduler.scheduleBackendSyncService(getApplicationContext(), 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();

        BackendSyncScheduler.cancelBackendSyncService(getApplicationContext());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        if(null != intent) {
            handleNewIntent(intent);
        }

        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        setBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
       drawerPresenter.navigationItemSelected(item, drawerLayout);
        return true;
    }


    @Override
    public void navigateUsingTo(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0 ; i < count ; ++i) {
            fm.popBackStack();
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment).commit();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.addToBackStack("MY_BACK_STACK");
        ft.commit();
    }

    @Override
    public void updateTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onSuccessOperation(int count) {

    }

    @Override
    public void onSuccessList(List<Car> carList) {
        Log.d("", "");

        if(null != carList && carList.size() > 0) {
            BookedCarDetailsFragment fragment = BookedCarDetailsFragment.newInstance();

            Bundle b = new Bundle();
            b.putSerializable(CommonConstants.CAR_OBJECT, carList.get(0));
            fragment.setArguments(b);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,fragment);
            ft.addToBackStack("MY_BACK_STACK");
            ft.commit();
        }
    }

    @Override
    public void onFailure(int errorCode) {
        String message = null;

        switch (errorCode) {
            case DatabaseErrorCodes.DATABASE_FAILURE:
                message = "Could not book car. Try after some time";
                break;

            case DatabaseErrorCodes.DATABASE_DUPLICATE:
                message = "Booking is already there";
                break;

            case DatabaseErrorCodes.DATABASE_NO_ENTRY:
                message = "Booking is not Available";
                break;

            default:
                break;

        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // setup views
    private void setupViews() {
        setSupportActionBar(toolbar);
        setUpActionBarDrawerToggle();
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setUpActionBarDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    /**
     *
     * @param intent
     */
    private void handleNewIntent(Intent intent) {
        String action = intent.getAction();

        if(CommonConstants.LAUNCH_BOOKING_DETAIL.equals(action)) {
            String bookingID = intent.getStringExtra(CommonConstants.CAR_BOOKING_ID);
            DatabaseWrapper.getBookingAsync(this, bookingID, this);
        }
    }
}
