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


        getLikedPostFromDatabase(new GetPostArrayData() {
            @Override
            public void dataLoaded(ArrayList<PostData> postDataList) {
                LikePostAdapter adapter = new LikePostAdapter(LikedAndFavActivity.this, android.R.layout.simple_list_item_1, postDataList);
                listview.setAdapter(adapter);
            }
        });
    }

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

    public interface GetPostArrayData {
        void dataLoaded(ArrayList<PostData> postDataList);
    }
}