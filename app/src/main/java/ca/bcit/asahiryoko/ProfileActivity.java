package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get reference to layout elements.
        profilePic = findViewById(R.id.profilePicture);
        username = findViewById(R.id.profile_Name);
        bio = findViewById(R.id.profile_Bio);

        // TODO: Read and display the user data from firebase.
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
                            //TODO: Add profile picture from database.
                            StorageReference storageImageLoc = storageRef.child((String) document.get("profilePicture"));
                            storageImageLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(ProfileActivity.this).load(uri).into(profilePic);
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
}