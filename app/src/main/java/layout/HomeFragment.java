package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.john.socialgolf.R;
import com.example.john.socialgolf.dataObjects.Users;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private static final String TAG = "UpdatePicture";
    ImageView mProfPicture;
    private String userID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mProfPicture = (ImageView) view.findViewById(R.id.profPicture);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView email = (TextView) view.findViewById(R.id.email);
        EditText aboutMe = (EditText) view.findViewById(R.id.aboutMeEdit);
        Button editSaveButton = (Button) view.findViewById(R.id.editTextSaveText);
        editSaveButton.setPaintFlags(editSaveButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        aboutMe.setEnabled(false);

        mProfPicture.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            Uri picture = user.getPhotoUrl();
            String displayName = user.getDisplayName();
            String displayEmail = user.getEmail();
            userID = user.getUid();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            // Create a reference to a file from a Google Cloud Storage URI
            if(picture != null){

                if(picture.toString().contains("googleusercontent")){
                    Glide.with(this)
                            .load(picture.toString())
                            .into(mProfPicture);
                }else {
                    StorageReference gsReference = storage.getReferenceFromUrl(picture.toString());

                    //mProfPicture.setImageURI(picture);
                    Glide.with(getContext())
                            .using(new FirebaseImageLoader())
                            .load(gsReference)
                            .into(mProfPicture);
                }
            }
            name.setText(displayName);
            email.setText(displayEmail);
        }

        editSaveButton.setOnClickListener(v -> {
            String text = editSaveButton.getText().toString();
            if(text.contentEquals("Edit Text")){
                aboutMe.setEnabled(true);
                editSaveButton.setText("Save Text");
            }else if(text.contentEquals("Save Text")){
                String about = aboutMe.getText().toString();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                database.child("users").child(userID).child("aboutMe").setValue(about);
                aboutMe.setEnabled(false);
                editSaveButton.setText("Edit Text");
            }
        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child(userID).getChildren()){
                    //String uid = ds.getValue();
                    if(ds.getKey().contentEquals("aboutMe")){
                        String me = ds.getValue().toString();
                        aboutMe.setText(me);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.addValueEventListener(userListener);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(contentUri)
                    .build();

            user.updateProfile(profileUpdates);
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d(TAG, "User profile updated.");
//                                mProfPicture.setImageURI(contentUri);
//                            }
//                        }
//                    });

            String uid = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Uri profPicture = null;

            while (profPicture == null){
                profPicture = user.getPhotoUrl();
            }
            if (profPicture != null){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();
                // Create a reference to "mountains.jpg"
                StorageReference mountainsRef = storageRef.child(mCurrentPhotoPath);

                try{
                    InputStream stream = new FileInputStream(new File(mCurrentPhotoPath));

                    UploadTask uploadTask = mountainsRef.putStream(stream);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            database.child("users").child(uid).child("picture").setValue(downloadUrl.toString());
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            // Create a storage reference from our app
                            // Create a reference to a file from a Google Cloud Storage URI
                            StorageReference gsReference = storage.getReferenceFromUrl(downloadUrl.toString());
                            //String path = gsReference.getPath();

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUrl)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                //mProfPicture.setImageURI(contentUri);
                                                Glide.with(getContext())
                                                        .using(new FirebaseImageLoader())
                                                        .load(gsReference)
                                                        .into(mProfPicture);
                                            }
                                        }
                                    });
                        }
                    });
                }catch(java.io.FileNotFoundException e){

                }
            }
        }

    }
}
