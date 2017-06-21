package com.example.john.socialgolf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.john.socialgolf.dataObjects.Conversation;
import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.GolfBuddiesContent;
import com.example.john.socialgolf.dataObjects.Messages;
import com.example.john.socialgolf.dataObjects.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewSendMessageActivity extends AppCompatActivity
                                    implements DisplayConversationFragment.OnListFragmentInteractionListener {

    private MultipleSelectionSpinner friendSpinner;
    private List<String> displayFriends;
    private List<Users> usersList;
    private List<Friends> friendsList;
    private static final String TAG = "SelectFriends";
    private EditText messageContent;
    public String messageKey;
    private Intent extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_send_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        friendSpinner = (MultipleSelectionSpinner) findViewById(R.id.send_to);
        messageContent = (EditText) findViewById(R.id.input_text);

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
                            if(user.picture != null) {
                                GolfBuddiesContent.GolfBuddiesItem item = new GolfBuddiesContent.GolfBuddiesItem(user.name, Uri.parse(user.picture), user.email);
                                displayFriends.add(item.email);
                                allFriends.add(item);
                            }else{
                                GolfBuddiesContent.GolfBuddiesItem item = new GolfBuddiesContent.GolfBuddiesItem(user.name, null, user.email);
                                displayFriends.add(item.email);
                                allFriends.add(item);
                            }
                        }
                    }
                }

                List<String> setVals = new ArrayList<String>();
                if(extras.hasExtra("members")){
                    Parcelable parcel = extras.getParcelableExtra("members");
                    List<Friends> members = Parcels.unwrap(parcel);

                    for(Friends member : members){
                        for(Users user: usersList){
                            if(member.uid.contentEquals(user.name)){
                                setVals.add(user.email);
                                break;
                            }
                        }
                    }
                }

                friendSpinner.setItems(displayFriends);
                friendSpinner.setSelection(setVals);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.addValueEventListener(userListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMessage();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extras = getIntent();
        if(extras.hasExtra("key")){
            String key = extras.getStringExtra("key");
            messageKey = key;
        }
    }

    private void addMessage(){
        Intent extras = getIntent();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String displayName = null;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        // if it has extra it is existing message
        if(extras.hasExtra("key") || messageKey != null){
            String key = extras.getStringExtra("key");
            Conversation convo = new Conversation();
            convo.lastMessage = messageContent.getText().toString();
            if(key != null){
                messageKey = key;
            }

            database.child("conversations").child(messageKey).child("lastMessage").setValue(convo.lastMessage);

            for (Users u : usersList) {
                if(u.uid.contentEquals(user.getUid())){
                    displayName = u.name;
                    break;
                }
            }

            Messages message = new Messages();
            message.timestamp = new Date().toString();
            message.message = messageContent.getText().toString();
            message.sender = displayName;

            database.child("messages").child(messageKey).push().setValue(message);
            messageContent.setText(null);
        }
        // if it doesn't it is a new message
        else{
            Conversation convo = new Conversation();
            long time = System.currentTimeMillis();
            convo._key = String.valueOf(time);
            convo.lastMessage = messageContent.getText().toString();
            convo.owner = user.getUid();
            messageKey = convo._key;

            List<Friends> groupMembers = new ArrayList<Friends>();
            List<String> selected = new ArrayList<String>();
            selected = friendSpinner.getSelectedStrings();

            if(selected.size() == 0){
                Toast.makeText(this, "Must select at least one conversation member!",
                        Toast.LENGTH_SHORT).show();
                return;
            }else {
                for (String s : selected) {
                    for (Users u : usersList) {
                        if (u.email.contentEquals(s)) {
                            Friends member = new Friends();
                            member.uid = u.uid;
                            groupMembers.add(member);
                            break;
                        }
                    }
                }
            }

            for (Users u : usersList) {
                if(u.uid.contentEquals(user.getUid())){
                    displayName = u.name;
                    break;
                }
            }

            convo.groupMembers = groupMembers;
            database.child("conversations").child(convo._key).setValue(convo);

            Messages message = new Messages();
            message.timestamp = new Date().toString();
            message.message = messageContent.getText().toString();
            message.sender = displayName;

            database.child("messages").child(convo._key).push().setValue(message);
            messageContent.setText("");
        }
    }

    public String getKey(){
        return messageKey;
    }

    @Override
    public void onListFragmentInteraction(Messages item) {
        System.out.println("Interact!");
    }
}
