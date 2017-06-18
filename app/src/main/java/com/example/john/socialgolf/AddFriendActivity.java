package com.example.john.socialgolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import layout.GolfBuddiesFragment;

public class AddFriendActivity extends AppCompatActivity {

    private Spinner mEmail;
    private Button mAddFriend;
    private static final String TAG = "AddFriends";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmail = (Spinner) findViewById(R.id.friendEmail);
        mAddFriend = (Button) findViewById(R.id.addButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAddFriend.setOnClickListener(v -> {
            addFriendsToDatabase();
        });

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> userEmails = new ArrayList<String>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    //String uid = ds.getValue();
                    Users users = ds.getValue(Users.class);
                    String email = users.email;
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    if(!uid.contentEquals(users.uid)) {
                        userEmails.add(email);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),  android.R.layout.simple_spinner_item, userEmails);
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                mEmail.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.addListenerForSingleValueEvent(userListener);
    }

    private void addFriendsToDatabase(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                List<Users> usersList = new ArrayList<Users>();
                String friendUid = null;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //String uid = ds.getValue();
                    Users users = ds.getValue(Users.class);
                    if(users.email.contentEquals(mEmail.getSelectedItem().toString())){
                        friendUid = users.uid;
                    }
                    usersList.add(users);
                }

                FirebaseUser user = mAuth.getCurrentUser();
                String currentUid = user.getUid();
                DatabaseReference addRef = FirebaseDatabase.getInstance().getReference("friends");

                if(currentUid != null && friendUid != null){
                    // for current user, set friend
                    Friends friend = new Friends(friendUid);
                    addRef.child(currentUid).child(friendUid).setValue(friend);

                    // for other user, set friend
                    Friends currFriend = new Friends(currentUid);
                    addRef.child(friendUid).child(currentUid).setValue(currFriend);

                    Intent toFriends = new Intent(AddFriendActivity.this, NavDrawerActivity.class);
                    setResult(GolfBuddiesFragment.ADDFRIENDRESULT, toFriends);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.addListenerForSingleValueEvent(userListener);
    }

}
