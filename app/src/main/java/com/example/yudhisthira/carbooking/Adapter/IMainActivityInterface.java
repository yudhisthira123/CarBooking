package com.example.yudhisthira.carbooking.Adapter;

import android.support.v4.app.Fragment;

/**
 * Created by yudhisthira on 17/05/17.
 */
public interface IMainActivityInterface {
    /**
     * Replace fragment.
     *
     * @param fragment the fragment
     */
    public void replaceFragment(Fragment fragment);

    /**
     * Update title.
     *
     * @param title the title
     */
    public void updateTitle(String title);
}
