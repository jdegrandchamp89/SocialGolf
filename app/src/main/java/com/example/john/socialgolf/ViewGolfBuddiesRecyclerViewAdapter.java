package com.example.john.socialgolf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.john.socialgolf.ViewGolfBuddiesFragment.OnListFragmentInteractionListener;
import com.example.john.socialgolf.dataObjects.GolfBuddiesContent;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ViewGolfBuddiesRecyclerViewAdapter extends RecyclerView.Adapter<ViewGolfBuddiesRecyclerViewAdapter.ViewHolder> {

    private List<GolfBuddiesContent.GolfBuddiesItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ViewGolfBuddiesRecyclerViewAdapter(List<GolfBuddiesContent.GolfBuddiesItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void reloadFrom(final List<GolfBuddiesContent.GolfBuddiesItem> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_golfbuddies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).name);
        holder.mEmail.setText(mValues.get(position).email);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl(mValues.get(position).picture.toString());
        String path = gsReference.getPath();
        Glide.with(holder.mView.getContext())
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .into(holder.mPicture);
        //holder.mPicture.setImageURI(Uri.parse("gs://socialgolf-57614.appspot.com" + path));//mValues.get(position).picture);

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
        public final TextView mName;
        public final TextView mEmail;
        public final ImageView mPicture;
        public GolfBuddiesContent.GolfBuddiesItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mEmail = (TextView) view.findViewById(R.id.email);
            mPicture = (ImageView) view.findViewById(R.id.profPicture);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mEmail + "'";
        }
    }
}
