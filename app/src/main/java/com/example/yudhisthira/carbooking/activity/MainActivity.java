package com.example.yudhisthira.carbooking.activity;

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
import android.view.Menu;
import android.view.MenuItem;

import com.example.yudhisthira.carbooking.Adapter.IMainActivityInterface;
import com.example.yudhisthira.carbooking.drawer.DrawerPresenterImpl;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DrawerPresenterImpl.DrawerView,
        IMainActivityInterface{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DrawerPresenterImpl drawerPresenter;

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
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.container,fragment);
        fm.addToBackStack("MY_BACK_STACK");
        fm.commit();
    }

    // setup views
    private void setupViews() {
        setSupportActionBar(toolbar);
        setUpActionBarDrawerToggle();
        navigationView.setNavigationItemSelectedListener(this);
    }

    // create a ActionBarDrawerToggle
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

    // setting for BackPressed
    private void setBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
