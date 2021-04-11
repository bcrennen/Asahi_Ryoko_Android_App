package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/***
 * This Activity is used to display the user's favorite post.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class LikedAndFavActivity extends AppCompatActivity {

    private ListView listview;

    /**
     * Get an instance of the Cloud Firestore.
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /** Create a Cloud Storage reference from the app */
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("Uploads");

    /**
     * Get a reference to firebase auth.
     */
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /** Get a reference to the real time database */
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("UserUploads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Liked / Favorite");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_and_fav);

        listview = findViewById(R.id.like_fav_post_list);

        //TODO: Get a list of Post from the database.
        //PostAdapter postAdapter = new PostAdapter();
        //listview.setAdapter(PostAdapter);
    }

    private ArrayList<PostData> getPostFromDatabase() {
        /** TEST: Gets data with filter, this class doesn't need filter.
         * This class gets data from user data. */

        mDatabaseReference.child("Leisure").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot document = task.getResult();
                    StorageReference storageImageLoc = mStorageReference.child((document.getValue(PostData.class).getImageUrl()));
                    storageImageLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Picasso.with(NatureListActivity.this).load(uri).into(testPhotoView);
                        }
                    });
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

    }
}