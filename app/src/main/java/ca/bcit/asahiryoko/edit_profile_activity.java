package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
/***
 * This Activity allows users to edit their Name, Bio and profile picture.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class edit_profile_activity extends AppCompatActivity {

    private final String TAG = "edit_profile_activity";
    private ImageView profilePic;
    private TextView username;
    private TextView bio;
    private LinearLayout nameLayout;
    private LinearLayout bioLayout;
    private final String USER_IMAGE_FOLDER = "UserImage";


    /**
     * Get an instance of the Cloud Firestore.
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Get a reference to firebase auth.
     */
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /** Create a Cloud Storage reference from the app */
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_profile_activity);

        profilePic = findViewById(R.id.edit_profile_picture);
        username = findViewById(R.id.editNameCurrentValue);
        bio = findViewById(R.id.editBioCurrentValue);
        nameLayout = findViewById(R.id.EditNameLayout);
        bioLayout = findViewById(R.id.EditBioLayout);


        profilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                editProfilePicture();
            }
        });

        nameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                editUsername();
            }
        });


        bioLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                editBio();
            }
        });

        displayCurrentProfile();
        realTimeUpdateDB();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent returnToProfile = new Intent(edit_profile_activity.this, ProfileActivity.class);
        startActivity(returnToProfile);
        return true;
    }

    /***
     * This method is responsible for displaying the current user data from the database.
     */
    private void displayCurrentProfile() {
        DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    // Begin reading data from firebase and displaying to UI elements.
                    try {
                        username.setText((String) document.get("username"));
                        bio.setText((String) document.get("bio"));

                        // If database doesn't have a profile picture, use a default one.
                        if (document.get("profilePicture") == null) {
                            profilePic.setImageDrawable(getDrawable(R.drawable.default_profile));
                        } else {

                            StorageReference storageImageLoc = storageRef.child(USER_IMAGE_FOLDER).child((String) document.get("profilePicture"));
                            storageImageLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(edit_profile_activity.this).load(uri).into(profilePic);
                                }
                            });

                        }


                    } catch(Exception e) {
                        Log.d(TAG, e.getMessage());
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

        private void realTimeUpdateDB() {
        DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    StorageReference storageImageLoc = storageRef.child(USER_IMAGE_FOLDER).child((String) snapshot.get("profilePicture"));
                    storageImageLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(edit_profile_activity.this).load(uri).into(profilePic);
                        }
                    });

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void editUsername() {
        EditFieldDialog nameDialog = new EditFieldDialog(username, "username");
        nameDialog.show(getSupportFragmentManager(), "Enter your name");

    }

    private void editBio() {
        EditFieldDialog nameDialog = new EditFieldDialog(bio, "bio");
        nameDialog.show(getSupportFragmentManager(), "Enter your bio description");

    }


    private void editProfilePicture() {
        int RESULT_LOAD_IMAGE = 1;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                String fileName = new File(filePath).getName();
                cursor.close();


                //Bitmap imageSelected = BitmapFactory.decodeFile(filePath);

                String storageUID = firebaseAuth.getCurrentUser().getUid() + "_" + fileName;

                UploadTask imageUT = storageRef.child("UserImage").child(storageUID).putFile(selectedImage);

                imageUT.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(edit_profile_activity.this, "Image successfully uploaded!", Toast.LENGTH_LONG).show();
                    }
                });

                imageUT.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(edit_profile_activity.this, "Something went wrong when uploading image.", Toast.LENGTH_LONG).show();
                    }
                });


                db.collection("users").document(firebaseAuth.getUid()).update("profilePicture", storageUID);

                profilePic.setImageURI(selectedImage);


        }
    }
}