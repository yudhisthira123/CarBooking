package com.example.yudhisthira.carbooking.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;


/**
 * The interface Drawer presenter.
 */
public interface DrawerPresenter {
    /**
     * Navigation item selected.
     *
     * @param item         the item
     * @param drawerLayout the drawer layout
     */
    void navigationItemSelected(MenuItem item, DrawerLayout drawerLayout);
}
