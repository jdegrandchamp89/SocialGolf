package com.example.john.socialgolf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.Users;
import com.example.john.socialgolf.dummy.MessageContent;
import com.example.john.socialgolf.dummy.MessageContent.MessageItem;
import com.example.john.socialgolf.dummy.GolfBuddiesContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ViewGolfBuddiesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<GolfBuddiesContent.GolfBuddiesItem> allFriends;
    private static final String TAG = "ListFriends";
    private ViewGolfBuddiesRecyclerViewAdapter recycler;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewGolfBuddiesFragment() {
        allFriends = new ArrayList<GolfBuddiesContent.GolfBuddiesItem>();
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ViewGolfBuddiesFragment newInstance(int columnCount) {
        ViewGolfBuddiesFragment fragment = new ViewGolfBuddiesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_golfbuddies_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    allFriends = new ArrayList<GolfBuddiesContent.GolfBuddiesItem>();
                    
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
                                allFriends.add(item);
                            }
                        }
                    }
                    recycler.reloadFrom(allFriends);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };

            database.addValueEventListener(userListener);

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //allFriends = GolfBuddiesContent.ITEMS;
            recycler = new ViewGolfBuddiesRecyclerViewAdapter(allFriends, mListener);
            recyclerView.setAdapter(recycler);

            DividerItemDecoration did = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(did);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(GolfBuddiesContent.GolfBuddiesItem item);
    }
}
