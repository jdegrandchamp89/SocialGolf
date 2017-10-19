package com.example.john.socialgolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.Users;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import layout.GolfBuddiesFragment;

public class AddFriendActivity extends AppCompatActivity {

    // removing spinner, refactoring to use ListView 10/17/2017
    //private Spinner mEmail;
    private Button mAddFriend;
    private static final String TAG = "AddFriends";
    private FirebaseAuth mAuth;

    // new ListView is here below 10/17/2017
    private ListView mFriendList;
    private EditText mSearchText;
    private Users selectedUser;
    private FirebaseListAdapter<Users> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // removing spinner, refactoring to use ListView 10/17/2017
        //mEmail = (Spinner) findViewById(R.id.friendEmail);
        mAddFriend = (Button) findViewById(R.id.addButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAddFriend.setOnClickListener(v -> {
            addFriendsToDatabase();
        });

        mAuth = FirebaseAuth.getInstance();

        // removing spinner, refactoring to use ListView 10/17/2017
        /*DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

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
        };*/

        //database.addListenerForSingleValueEvent(userListener);

        // in this method is the new code to populate list view 10/17/2017
        populateListView();
        // in this method is the new code to search the list view when text changes 10/19/2017
        triggerSearchListView();
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
                    // change this code to work with selected user from friend list 10/19/2017
                    Users users = ds.getValue(Users.class);
                    if(selectedUser != null) {
                        if (users.email.contentEquals(selectedUser.email)) {
                            friendUid = users.uid;
                        }
                        usersList.add(users);
                    }
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

    // this is the "sprouted" method for the new ListView that has been added for displaying
    // potential friends to add 10/17/2017
    private void populateListView(){

        mSearchText = (EditText) findViewById(R.id.searchText);
        String search = mSearchText.getText().toString();
        mFriendList = (ListView) findViewById(R.id.friendList);
        Query query = null;
        DatabaseReference database = null;

        if(!search.contentEquals("")){
            query = FirebaseDatabase.getInstance().getReference("users").orderByChild("name").startAt(search);
        }
        else{
            database = FirebaseDatabase.getInstance().getReference("users");
        }

       /* FirebaseListOptions<Users> options = new FirebaseListOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();*/

        if(!search.contentEquals("")) {
            adapter = new FirebaseListAdapter<Users>(this, Users.class, android.R.layout.simple_list_item_2, query) {
                @Override
                protected void populateView(View v, Users model, int position) {
                    TextView name = (TextView) v.findViewById(android.R.id.text1);
                    name.setText(model.name);
                    TextView email = (TextView) v.findViewById(android.R.id.text2);
                    email.setText(model.email);
                }
            };
        }
        else{
            adapter = new FirebaseListAdapter<Users>(this, Users.class, android.R.layout.simple_list_item_2, database) {

                @Override
                protected void populateView(View v, Users model, int position) {
                    TextView name = (TextView) v.findViewById(android.R.id.text1);
                    name.setText(model.name);
                    TextView email = (TextView) v.findViewById(android.R.id.text2);
                    email.setText(model.email);
                }

            };
        }

        mFriendList.setAdapter(adapter);
        mFriendList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mFriendList.setSelection(position);
                selectedUser = (Users) adapter.getItem(position);
            }
        });
    }

    private void triggerSearchListView(){
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // this will call populate list view again hopefully triggering a new list view
                populateListView();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}