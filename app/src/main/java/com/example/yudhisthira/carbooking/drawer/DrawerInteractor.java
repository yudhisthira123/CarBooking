package com.example.yudhisthira.carbooking.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Created by Jhordan on 13/10/15.
 */
public interface DrawerInteractor {
    void navigateTo(MenuItem item, DrawerLayout drawerLayout, DrawerListener listener);
}
