package com.example.yudhisthira.carbooking.drawer;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * The type Drawer presenter.
 */
public class DrawerPresenterImpl implements DrawerPresenter, DrawerListener {

    /**
     * The Drawer interactor.
     */
    DrawerInteractorImpl drawerInteractor;
    /**
     * The Drawer view.
     */
    DrawerView drawerView;

    /**
     * Instantiates a new Drawer presenter.
     *
     * @param drawerView the drawer view
     */
    public DrawerPresenterImpl(DrawerView drawerView) {
        this.drawerView = drawerView;
        drawerInteractor = new DrawerInteractorImpl();
    }

    @Override
    public void navigationItemSelected(MenuItem item, DrawerLayout drawerLayout) {
        drawerInteractor.navigateTo(item, drawerLayout, this);
    }

    @Override
    public void fragmentReplace(Fragment fragment) {
        drawerView.navigateUsingTo(fragment);
    }

    /**
     * The interface Drawer view.
     */
    public interface DrawerView {
        /**
         * Navigate using to.
         *
         * @param fragment the fragment
         */
        void navigateUsingTo(Fragment fragment);
    }

}
