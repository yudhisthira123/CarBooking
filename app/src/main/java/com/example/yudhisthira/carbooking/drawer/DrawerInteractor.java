package com.example.yudhisthira.carbooking.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * The interface Drawer interactor.
 */
public interface DrawerInteractor {
    /**
     * Navigate to.
     *
     * @param item         the item
     * @param drawerLayout the drawer layout
     * @param listener     the listener
     */
    void navigateTo(MenuItem item, DrawerLayout drawerLayout, DrawerListener listener);
}
