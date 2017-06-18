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

import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.TeeTimeItem;
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
public class TeeTimesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<TeeTimeItem> teeTimeList;
    private static final String TAG = "ListTeeTimes";
    private TeeTimesRecyclerViewAdapter recycler;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeeTimesFragment() {
        teeTimeList = new ArrayList<TeeTimeItem>();
    }

    // TODO: Customize parameter initialization
    public static TeeTimesFragment newInstance(int columnCount) {
        TeeTimesFragment fragment = new TeeTimesFragment();
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
        View view = inflater.inflate(R.layout.fragment_tee_times_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            DatabaseReference database = FirebaseDatabase.getInstance().getReference("teeTimes");

            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    teeTimeList = new ArrayList<TeeTimeItem>();

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser userFb = mAuth.getCurrentUser();
                    String uid = userFb.getUid();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        //String uid = ds.getValue();
                        TeeTimeItem teeTime = ds.getValue(TeeTimeItem.class);
                        if(teeTime.owner.contentEquals(uid)){
                            teeTimeList.add(0, teeTime);
                        }else{
                            if(teeTime.groupMembers != null) {
                                for (Friends member : teeTime.groupMembers) {
                                    if (member.uid.contentEquals(uid)) {
                                        teeTimeList.add(0, teeTime);
                                        break;
                                    }
                                }
                            }
                        }

                    }

                    recycler.reloadFrom(teeTimeList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };

            database.addValueEventListener(userListener);

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            teeTimeList = new ArrayList<TeeTimeItem>();
            recycler = new TeeTimesRecyclerViewAdapter(teeTimeList, mListener);
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
        void onListFragmentInteraction(TeeTimeItem item);
    }
}
