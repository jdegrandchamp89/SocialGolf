package com.example.john.socialgolf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.socialgolf.ViewMessagesFragment.OnListFragmentInteractionListener;
import com.example.john.socialgolf.dataObjects.Conversation;
import com.example.john.socialgolf.dataObjects.Friends;
import com.example.john.socialgolf.dataObjects.TeeTimeItem;
import com.example.john.socialgolf.dummy.MessageContent;
import com.example.john.socialgolf.dummy.MessageContent.MessageItem;

import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ViewMessagesRecyclerViewAdapter extends RecyclerView.Adapter<ViewMessagesRecyclerViewAdapter.ViewHolder> {

    private List<Conversation> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ViewMessagesRecyclerViewAdapter(List<Conversation> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_messages, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String displayValue = new String();
        holder.mItem = mValues.get(position);
        for(Friends member : mValues.get(position).groupMembers){
            int i = 0;

            if(i == 0){
                displayValue = member.uid.toString();
            }else {
                displayValue = displayValue + "," + member.uid.toString();
            }
            i++;
        }
        holder.mIdView.setText(displayValue);
        holder.mContentView.setText(mValues.get(position).lastMessage);

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

    public void reloadFrom(final List<Conversation> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Conversation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
