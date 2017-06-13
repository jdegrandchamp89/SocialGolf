package com.example.john.socialgolf;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.Users;
import com.example.john.socialgolf.dummy.GolfBuddiesContent;
import com.foursquare.api.types.Venue;
import com.foursquare.placepicker.PlacePicker;
import com.foursquare.placepicker.PlacePickerSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

public class AddTeeTimeActivity extends AppCompatActivity {

    private static final String TAG = "SelectFriends";
    private List<String> displayFriends;
    private static final String CONSUMER_KEY = "NTH4US0YOQH5V00JF13B5VRMMV4GVFVQXNRDN1CW0UZKXDUI";
    private static final String CONSUMER_SECRET = "U2PVQBCHHY13TJSPY2RRJOE4C2OMEYYIFDUQLA2PPDTJXM5S";
    private EditText golfCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tee_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MultipleSelectionSpinner friendSpinner = (MultipleSelectionSpinner) findViewById(R.id.friendSpinner);
        golfCourse = (EditText) findViewById(R.id.golfCourse);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] friendInfo;
                // Get Post object and use the values to update the UI
                List<GolfBuddiesContent.GolfBuddiesItem> allFriends = new ArrayList<GolfBuddiesContent.GolfBuddiesItem>();
                displayFriends = new ArrayList<String>();
                List<Friends> friendsList = new ArrayList<Friends>();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser userFb = mAuth.getCurrentUser();
                String uid = userFb.getUid();
                for(DataSnapshot ds : dataSnapshot.child("friends").child(uid).getChildren()){
                    //String uid = ds.getValue();
                    Friends friend = ds.getValue(Friends.class);
                    friendsList.add(friend);
                }

                List<Users> usersList = new ArrayList<Users>();
                for(DataSnapshot ds : dataSnapshot.child("users").getChildren()){
                    //String uid = ds.getValue();
                    Users users = ds.getValue(Users.class);
                    usersList.add(users);
                }

                for (Friends friend : friendsList) {
                    for (Users user : usersList) {
                        if(friend.uid.contentEquals(user.uid)){
                            GolfBuddiesContent.GolfBuddiesItem item = new GolfBuddiesContent.GolfBuddiesItem(user.name, Uri.parse(user.picture), user.email);
                            displayFriends.add(item.email);
                            allFriends.add(item);
                        }
                    }
                }

                friendSpinner.setItems(displayFriends);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.addValueEventListener(userListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PlacePickerSdk.with(new PlacePickerSdk.Builder(this)
                .consumer(CONSUMER_KEY, CONSUMER_SECRET)
                .imageLoader(new PlacePickerSdk.ImageLoader() {
                    @Override
                    public void loadImage(Context context, ImageView v, String url) {
                        Glide.with(context)
                                .load(url)
                                .placeholder(R.drawable.category_none)
                                .dontAnimate()
                                .into(v);
                    }
                })
                .build());

        golfCourse.setOnClickListener(v -> {
            pickPlace();
        });
    }

    private void pickPlace() {
        if (mayRequestLocation()) {
            Intent intent = new Intent(this, PlacePicker.class);
            startActivityForResult(intent, 9001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == PlacePicker.PLACE_PICKED_RESULT_CODE) {
            Venue place = data.getParcelableExtra(PlacePicker.EXTRA_PLACE);
            golfCourse.setText(place.getName());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            Snackbar.make(golfCourse, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 0);
                        }
                    });
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 0);
        }
        return false;
    }

}
