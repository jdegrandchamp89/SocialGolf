package com.example.john.socialgolf.dummy;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.john.socialgolf.AddFriendActivity;
import com.example.john.socialgolf.NavDrawerActivity;
import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.GolfBuddiesFragment;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GolfBuddiesContent {

    public static final List<GolfBuddiesContent.GolfBuddiesItem> ITEMS = new ArrayList<GolfBuddiesContent.GolfBuddiesItem>();
    private static final String TAG = "ListFriends";

    public static void addItem(GolfBuddiesContent.GolfBuddiesItem item) {
        ITEMS.add(item);
    }
    static {

//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//
//        ValueEventListener userListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                List<Friends> friendsList = new ArrayList<Friends>();
//                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                FirebaseUser userFb = mAuth.getCurrentUser();
//                String uid = userFb.getUid();
//                for(DataSnapshot ds : dataSnapshot.child("friends").child(uid).getChildren()){
//                    //String uid = ds.getValue();
//                    Friends friend = ds.getValue(Friends.class);
//                    friendsList.add(friend);
//                }
//
//                List<Users> usersList = new ArrayList<Users>();
//                for(DataSnapshot ds : dataSnapshot.child("users").getChildren()){
//                    //String uid = ds.getValue();
//                    Users users = ds.getValue(Users.class);
//                    usersList.add(users);
//                }
//
//                for (Friends friend : friendsList) {
//                    for (Users user : usersList) {
//                        if(friend.uid.contentEquals(user.uid)){
//                            addItem(new GolfBuddiesContent.GolfBuddiesItem(user.name, Uri.parse(user.picture), user.email));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//
//        database.addValueEventListener(userListener);
//        addItem(new GolfBuddiesContent.GolfBuddiesItem("Phil Backers", 54));
    }

    public static class GolfBuddiesItem {

        public final String name;
        public final Uri picture;
        public final String email;

        public GolfBuddiesItem(String name, Uri picture, String email) {

            this.name = name;
            this.picture = picture;
            this.email = email;

        }

        @Override
        public String toString() {return "(" + this.name + "," + this.email + ")";

        }
    }
}
