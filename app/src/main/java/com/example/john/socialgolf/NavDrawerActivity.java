package com.example.john.socialgolf;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.john.socialgolf.dummy.MessageContent;
import com.example.john.socialgolf.dummy.GolfBuddiesContent;
import com.example.john.socialgolf.dummy.TeeTimeContent;

import layout.GolfBuddiesFragment;
import layout.HomeFragment;
import layout.MessagesFragment;
import layout.MyTeeTimesFragment;
import layout.SettingsFragment;

public class NavDrawerActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    HomeFragment.OnFragmentInteractionListener,
                    GolfBuddiesFragment.OnFragmentInteractionListener,
                    MyTeeTimesFragment.OnFragmentInteractionListener,
                    MessagesFragment.OnFragmentInteractionListener,
                    SettingsFragment.OnFragmentInteractionListener,
                    TeeTimesFragment.OnListFragmentInteractionListener,
                    ViewMessagesFragment.OnListFragmentInteractionListener,
                    ViewGolfBuddiesFragment.OnListFragmentInteractionListener{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).commit();
        }

        mTitle = mDrawerTitle = getTitle();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to on PrepareOptionsMenu()
            }
        };

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        // Activate the navigation drawer toggle
        if (toggle.onOptionsItemSelected(item)) {
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
        Class fragmentClass = null;

        if (id == R.id.nav_home) {
            mTitle = "SocialGolf";
            getSupportActionBar().setTitle(mTitle);

            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_golf_buddies) {
            mTitle = "Golf Buddies";
            getSupportActionBar().setTitle(mTitle);

            fragmentClass = GolfBuddiesFragment.class;
        } else if (id == R.id.nav_tee_times) {
            mTitle = "My Tee Times";
            getSupportActionBar().setTitle(mTitle);

            fragmentClass = MyTeeTimesFragment.class;
        } else if (id == R.id.nav_messages) {
            mTitle = "Messages";
            getSupportActionBar().setTitle(mTitle);

            fragmentClass = MessagesFragment.class;
        } else if (id == R.id.nav_settings) {
            mTitle = "Settings";
            getSupportActionBar().setTitle(mTitle);

            fragmentClass = SettingsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(TeeTimeContent.TeeTimeItem item) {
        System.out.println("Interact!");
    }

    @Override
    public void onListFragmentInteraction(GolfBuddiesContent.GolfBuddiesItem item) {
        System.out.println("Interact!");
    }

    @Override
    public void onListFragmentInteraction(MessageContent.MessageItem item) {
        System.out.println("Interact!");
    }
}
