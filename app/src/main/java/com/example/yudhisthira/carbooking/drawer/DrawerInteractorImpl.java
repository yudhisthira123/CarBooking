package com.example.yudhisthira.carbooking.drawer;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.example.yudhisthira.carbooking.activity.R;
import com.example.yudhisthira.carbooking.fragment.BookedCarListFragment;
import com.example.yudhisthira.carbooking.fragment.CarListFragment;

/**
 * Created by Jhordan on 13/10/15.
 */
public class DrawerInteractorImpl implements DrawerInteractor {

    @Override
    public void navigateTo(MenuItem item, DrawerLayout drawerLayout, DrawerListener listener) {

        switch (item.getItemId()) {
            case R.id.nav_available_cars:
                listener.fragmentReplace(CarListFragment.newInstance());
                break;

            case R.id.nav_booked_cars:
                listener.fragmentReplace(BookedCarListFragment.newInstance());
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
