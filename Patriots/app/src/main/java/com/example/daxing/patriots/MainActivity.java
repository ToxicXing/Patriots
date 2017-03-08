package com.example.daxing.patriots;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    android.support.v4.app.FragmentManager mFragmentManager;
    android.support.v4.app.FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hamburger button and settings button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // navigation panel
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // make hamburger button toggleable
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // instantiate navigation view and add selected listener
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // create fragment transaction to display inital fragment
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new LegislatorsFragment()).commit();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (id == R.id.nav_legis) {
            Log.i("main", "Legislators selected");

            fragmentTransaction.replace(R.id.containerView,new LegislatorsFragment()).commit();

        } else if (id == R.id.nav_bills) {
            Log.e("main", "Bills selected");

            fragmentTransaction.replace(R.id.containerView,new BillsFragment()).commit();

        } else if (id == R.id.nav_coms) {
            Log.i("main", "committees selected");

            fragmentTransaction.replace(R.id.containerView,new CommitteesFragment()).commit();

        } else if (id == R.id.nav_favs) {
            Log.i("main", "favorites selected");

            fragmentTransaction.replace(R.id.containerView,new FavoritesFragment()).commit();

        } else if (id == R.id.nav_about) {
            Log.i("main", "about me selected");
            Intent intent = new Intent(this, AboutMe.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
