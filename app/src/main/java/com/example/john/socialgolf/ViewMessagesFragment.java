package com.example.john.socialgolf;

import android.content.Context;
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

import com.example.john.socialgolf.dataObjects.Conversation;
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

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ViewMessagesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Conversation> conversationList;
    private ViewMessagesRecyclerViewAdapter recycler;
    private List<Users> usersList;
    private static final String TAG = "ViewConversations";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewMessagesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ViewMessagesFragment newInstance(int columnCount) {
        ViewMessagesFragment fragment = new ViewMessagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_messages_list, container, false);

        conversationList = new ArrayList<Conversation>();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    conversationList = new ArrayList<Conversation>();
                    // Get Post object and use the values to update the UI
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser userFb = mAuth.getCurrentUser();
                    String uid = userFb.getUid();

                    usersList = new ArrayList<Users>();
                    for(DataSnapshot ds : dataSnapshot.child("users").getChildren()){
                        //String uid = ds.getValue();
                        Users users = ds.getValue(Users.class);
                        usersList.add(users);
                    }

                    for(DataSnapshot ds : dataSnapshot.child("conversations").getChildren()){
                        Conversation convo = ds.getValue(Conversation.class);
                        if(convo.owner.contentEquals(uid)){
                            for (Friends member : convo.groupMembers) {
                                for (Users user : usersList){
                                    if(member.uid.contentEquals(user.uid)){
                                        member.uid = user.name;
                                        break;
                                    }
                                }
                            }
                            conversationList.add(convo);
                        }else{
                            for (Friends member : convo.groupMembers){
                                if(member.uid.contentEquals(uid)){
                                    for (Friends member2 : convo.groupMembers) {
                                        for (Users user : usersList){
                                            if(member2.uid.contentEquals(user.uid)){
                                                member2.uid = user.name;
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            conversationList.add(convo);
                        }
                    }

                    recycler.reloadFrom(conversationList);
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
            recycler = new ViewMessagesRecyclerViewAdapter(conversationList, mListener);
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
        void onListFragmentInteraction(Conversation item);
    }
}
