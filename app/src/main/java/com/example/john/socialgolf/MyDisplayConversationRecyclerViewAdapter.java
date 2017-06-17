package com.example.john.socialgolf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.socialgolf.DisplayConversationFragment.OnListFragmentInteractionListener;
import com.example.john.socialgolf.dataObjects.Messages;
import com.example.john.socialgolf.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDisplayConversationRecyclerViewAdapter extends RecyclerView.Adapter<MyDisplayConversationRecyclerViewAdapter.ViewHolder> {

    private List<Messages> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyDisplayConversationRecyclerViewAdapter(List<Messages> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void reloadFrom(final List<Messages> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_displayconversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).timestamp);
        holder.mContentView.setText(mValues.get(position).message);
        holder.mSenderView.setText(mValues.get(position).sender);

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
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mSenderView;
        public Messages mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mSenderView = (TextView) view.findViewById(R.id.senderName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
