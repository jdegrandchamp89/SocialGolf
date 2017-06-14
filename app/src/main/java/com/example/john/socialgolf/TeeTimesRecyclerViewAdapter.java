package com.example.john.socialgolf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.socialgolf.TeeTimesFragment.OnListFragmentInteractionListener;
import com.example.john.socialgolf.dataObjects.TeeTimeItem;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TeeTimeItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TeeTimesRecyclerViewAdapter extends RecyclerView.Adapter<TeeTimesRecyclerViewAdapter.ViewHolder> {

    private final List<TeeTimeItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TeeTimesRecyclerViewAdapter(List<TeeTimeItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tee_times, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        SimpleDateFormat localDateFormat = new SimpleDateFormat("MM/dd/yy");
        holder.mDateView.setText(localDateFormat.format(mValues.get(position).teeTimeDate));

        holder.mTimeView.setText(mValues.get(position).teeTimeTime);
        holder.mGroupMembers.setText(Integer.toString(mValues.get(position).groupMembers.size()));
        holder.mBooked.setText(mValues.get(position).booked);
        holder.mCourse.setText(mValues.get(position).course);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mTimeView;
        public final TextView mGroupMembers;
        public final TextView mBooked;
        public final TextView mCourse;

        public TeeTimeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.date);
            mTimeView = (TextView) view.findViewById(R.id.time);
            mGroupMembers = (TextView) view.findViewById(R.id.groupMembers);
            mBooked = (TextView) view.findViewById(R.id.booked);
            mCourse = (TextView) view.findViewById(R.id.course);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}
