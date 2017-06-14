package com.example.john.socialgolf;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.TeeTimeItem;
import com.example.john.socialgolf.dataObjects.Users;
import com.example.john.socialgolf.dataObjects.GolfBuddiesContent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import layout.GolfBuddiesFragment;
import layout.MyTeeTimesFragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddTeeTimeActivity extends AppCompatActivity
                                implements DatePickerDialog.OnDateSetListener,
                                           TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "SelectFriends";
    private List<String> displayFriends;
    private static final String CONSUMER_KEY = "NTH4US0YOQH5V00JF13B5VRMMV4GVFVQXNRDN1CW0UZKXDUI";
    private static final String CONSUMER_SECRET = "U2PVQBCHHY13TJSPY2RRJOE4C2OMEYYIFDUQLA2PPDTJXM5S";
    private EditText golfCourse;
    private Button addTeeTimeButton;
    private List<Friends> friendsList;
    private MultipleSelectionSpinner friendSpinner;
    private EditText teeTimeTime;
    private EditText teeTimeDate;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tee_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        friendSpinner = (MultipleSelectionSpinner) findViewById(R.id.friendSpinner);
        golfCourse = (EditText) findViewById(R.id.golfCourse);
        addTeeTimeButton = (Button) findViewById(R.id.createTeeTime);
        teeTimeDate = (EditText) findViewById(R.id.teeTimeDate);
        teeTimeTime = (EditText) findViewById(R.id.teeTimeTime);
        ButterKnife.bind(this);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] friendInfo;
                // Get Post object and use the values to update the UI
                List<GolfBuddiesContent.GolfBuddiesItem> allFriends = new ArrayList<GolfBuddiesContent.GolfBuddiesItem>();
                displayFriends = new ArrayList<String>();
                friendsList = new ArrayList<Friends>();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser userFb = mAuth.getCurrentUser();
                String uid = userFb.getUid();
                for(DataSnapshot ds : dataSnapshot.child("friends").child(uid).getChildren()){
                    //String uid = ds.getValue();
                    Friends friend = ds.getValue(Friends.class);
                    friendsList.add(friend);
                }

                usersList = new ArrayList<Users>();
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

        addTeeTimeButton.setOnClickListener(v -> {
            addTeeTimeDB();
        });
    }

    private void pickPlace() {
        if (mayRequestLocation()) {
            Intent intent = new Intent(this, PlacePicker.class);
            intent.putExtra(PlacePicker.EXTRA_QUERY, "golf course");
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

    private void addTeeTimeDB(){
        DatabaseReference addRef = FirebaseDatabase.getInstance().getReference("teeTimes");

        TeeTimeItem item = new TeeTimeItem();
        item.course = golfCourse.getText().toString();
        item.teeTimeDate = teeTimeDate.getText().toString();
        item.teeTimeTime = teeTimeTime.getText().toString();

        List<Friends> members = new ArrayList<Friends>();
        Friends member = new Friends();
        List<String> selected = new ArrayList<String>();
        selected = friendSpinner.getSelectedStrings();

        for (String s : selected) {
            for (Users u : usersList) {
                if(u.email.contentEquals(s)){
                    member.uid = u.uid;
                    members.add(member);
                    break;
                }
            }
        }

        item.groupMembers = members;
        addRef.setValue(item);

        Intent toTeeTimes = new Intent(AddTeeTimeActivity.this, NavDrawerActivity.class);
        setResult(MyTeeTimesFragment.ADD_TEE_TIME, toTeeTimes);
        finish();
    }

    @OnClick(R.id.teeTimeDate)
    public void datePressed(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        Date d = new Date(year, month, day);
        teeTimeDate.setText(dateFormat.format(d));
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),
                    year, month, day);
        }

    }

    @OnClick(R.id.teeTimeTime)
    public void timePressed(){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
        cal.set(Calendar.MINUTE,minute);

        Date d = cal.getTime();
        teeTimeTime.setText(dateFormat.format(d));
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            /// Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(),
                    hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

    }
}
