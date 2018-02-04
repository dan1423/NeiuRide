package com.app.danny.neiuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.danny.neiuber.menu.items.DriverProfileMenu;
import com.app.danny.neiuber.menu.items.HelpMenu;
import com.app.danny.neiuber.menu.items.PaymentMenu;
import com.app.danny.neiuber.menu.items.RideHistoryMenu;
import com.app.danny.neiuber.menu.items.MapMenu;
import com.app.danny.neiuber.menu.items.SettingsMenu;

import java.util.HashMap;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private HashMap<String, String>hashMapOfUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //get hashmap passed from login page
         Intent in = getIntent();
         hashMapOfUserInfo = (HashMap<String,String>)in.getSerializableExtra("driver_info");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       Fragment mainFragment = new MapMenu();
        setFragment(mainFragment);
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_layout_screen, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_home) {
           fragment = new MapMenu();

        }else if(id == R.id.nav_profile) {
            fragment = new DriverProfileMenu();

        }else if (id == R.id.nav_ride_history) {
            fragment = new RideHistoryMenu();
        }else if(id == R.id.nav_settings) {
            fragment = new SettingsMenu();
        }
        else if (id == R.id.nav_help) {
            fragment = new HelpMenu();
        } else if (id == R.id.nav_payment) {
            fragment = new PaymentMenu();
        }

        if(fragment != null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_layout_screen, fragment);

            //send driver info to selected fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("driver_info", hashMapOfUserInfo);
            fragment.setArguments(bundle);

            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
