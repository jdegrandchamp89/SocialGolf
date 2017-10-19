package com.example.john.socialgolf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.john.socialgolf.dataObjects.Conversation;
import com.example.john.socialgolf.dataObjects.GolfBuddiesContent;
import com.example.john.socialgolf.dataObjects.TeeTimeItem;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

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
    private final int VIEW_MESSAGES = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

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

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name, email address, and profile photo Url
                String name = user.getDisplayName();
                String email = user.getEmail();
                Uri picture = user.getPhotoUrl();

                TextView displayName = (TextView) headerView.findViewById(R.id.name);
                TextView displayEmail = (TextView) headerView.findViewById(R.id.email);
                ImageView profPicture = (ImageView) headerView.findViewById(R.id.profPicture);

                displayName.setText(name);
                displayEmail.setText(email);
                //profPicture.setImageURI(picture);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                // Create a reference to a file from a Google Cloud Storage URI
                if(picture != null){
                    StorageReference gsReference = storage.getReferenceFromUrl(picture.toString());

                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(gsReference)
                            .into(profPicture);
                }
            }
        }

        mTitle = mDrawerTitle = getTitle();

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

        if (item.getItemId() == R.id.action_log_off) {
            FirebaseAuth.getInstance().signOut();
            Intent toLogin = new Intent(NavDrawerActivity.this, LoginActivity.class);
            startActivity(toLogin);
            finish();

            return true;
        }

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
            mTitle = "Friends";
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
    public void onListFragmentInteraction(TeeTimeItem item) {

        //System.out.println("Interact!");
    }

    @Override
    public void onListFragmentInteraction(GolfBuddiesContent.GolfBuddiesItem item) {
        //System.out.println("Interact!");
    }

    @Override
    public void onListFragmentInteraction(Conversation item) {
        Intent toConvo = new Intent(this, ViewSendMessageActivity.class);
        toConvo.putExtra("key", item._key);
        Parcelable parcel = Parcels.wrap(item.groupMembers);
        toConvo.putExtra("members", parcel);
        startActivityForResult(toConvo, VIEW_MESSAGES);
    }
}
