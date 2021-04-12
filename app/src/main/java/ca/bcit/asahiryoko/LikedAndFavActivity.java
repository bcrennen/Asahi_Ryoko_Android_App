package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/***
 * This Activity is used to display the user's favorite post.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class LikedAndFavActivity extends AppCompatActivity {

    private ListView listview;
    private final String LIKED_POSTS = "liked_posts";

    /**
     * Get an instance of the Cloud Firestore.
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Create a Cloud Storage reference from the app
     */
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("Uploads");

    /**
     * Get a reference to firebase auth.
     */
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /**
     * Get a reference to the real time database
     */
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("UserUploads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Liked / Favorite");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_and_fav);

        listview = findViewById(R.id.like_fav_post_list);

        // Set up the bottom nav bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),
                                NewSearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.like:

                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),
                                ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }

        });


        getLikedPostFromDatabase(new GetPostArrayData() {
            @Override
            public void dataLoaded(ArrayList<PostData> postDataList) {
                LikePostAdapter adapter = new LikePostAdapter(LikedAndFavActivity.this, android.R.layout.simple_list_item_1, postDataList);
                listview.setAdapter(adapter);
            }
        });
    }

    /**
     * This method gets the data from the firestorm database and updates the Views.
     * Reads the user's liked Posts and outputs it to an Listview.
     *
     * Some help was used because the ArrayList wasn't properly updated.
     * SOURCE: https://stackoverflow.com/questions/50110780/list-not-updating-when-fetching-data-from-firebase-android
     * @param post_data
     */
    private void getLikedPostFromDatabase(GetPostArrayData post_data) {

        DocumentReference currentUserDB = db.collection("users").document(firebaseAuth.getUid());
        ArrayList<PostData> currentLiked = new ArrayList<>();

        // Get a list of currently liked Posts.
        currentUserDB.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  DocumentSnapshot document = task.getResult();

                  for( HashMap item : (List<HashMap>) document.get(LIKED_POSTS)) {

                      currentLiked.add( new PostData(item.get("description").toString(), item.get("id").toString(),
                              item.get("imageUrl").toString(), item.get("placeName").toString()) );
                  }

                  post_data.dataLoaded(currentLiked);

              }
        });





    }

    /**
     * An interfaced used to help update the ArrayList.
     * Same source as getLikedPostFromDatabase()
     * Source: https://stackoverflow.com/questions/50110780/list-not-updating-when-fetching-data-from-firebase-android
     */
    public interface GetPostArrayData {
        void dataLoaded(ArrayList<PostData> postDataList);
    }
}