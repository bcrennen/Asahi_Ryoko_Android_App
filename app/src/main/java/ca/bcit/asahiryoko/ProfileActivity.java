package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.net.URL;

/***
 * This Activity is used to display user data and recently visited places.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivity";
    private ImageView profilePic;
    private TextView username;
    private TextView bio;
    private ImageButton editProfileButton;
    private final String USER_IMAGE_FOLDER = "UserImage";
    /**
     * Get an instance of the Cloud Firestore.
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /** Create a Cloud Storage reference from the app */
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    /**
     * Get a reference to firebase auth.
     */
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private FloatingActionButton postFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get reference to layout elements.
        profilePic = findViewById(R.id.profilePicture);
        username = findViewById(R.id.profile_Name);
        bio = findViewById(R.id.profile_Bio);
        editProfileButton = findViewById(R.id.profileEditButton);


        // Set up Nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);

        postFab = findViewById(R.id.postFloat);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0,0);

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),
                                NewSearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.like:
                        startActivity(new Intent(getApplicationContext(),
                                LikedAndFavActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        return true;

                }
                return false;
            }

        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEdit();
            }
        });

        displayUserInfo();


    }

    /**
     * This method is responsible for displaying the current user's information.
     * The information is read from the database and written on Views.
     */
    private void displayUserInfo() {
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
                                    Glide.with(ProfileActivity.this).load(uri).into(profilePic);
                                }
                            });

                            storageImageLoc.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.getMessage());
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


    /***
     * This prompts the edit profile activity.
     * This activity allows the user to make changes to their profile.
     */
    private void startEdit() {
        Intent editActivity = new Intent(ProfileActivity.this, edit_profile_activity.class);
        startActivity(editActivity);
    }

}