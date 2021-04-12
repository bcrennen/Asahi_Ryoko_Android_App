package ca.bcit.asahiryoko;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/***
 * This adapter uses a Post object and the database to display
 * information to the list view.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class PostAdapter extends ArrayAdapter<PostData> {
    private final String TAG = "PostAdapter";
    private final String LIKED_POSTS = "liked_posts";
    private ArrayList<PostData> postList;
    private Context context;


    /**
     * Get a reference to the firebase storage.
     */
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("Uploads");

    /**
     * Get an reference to the firebase Authentication
     */
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /**
     * Get an instance of the Cloud Firestore.
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PostData> objects) {
        super(context, resource, objects);
        this.postList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_template, parent, false);
        }


        TextView postName = convertView.findViewById(R.id.post_name);
        TextView postDesc = convertView.findViewById(R.id.post_desc);
        ImageView postPic = convertView.findViewById(R.id.post_pic);
        ImageView saveButton = convertView.findViewById(R.id.removeFavPost);

        postName.setText(postList.get(position).getPlaceName());
        postDesc.setText(postList.get(position).getDescription());

        // Get the image from the firebase storage.

        StorageReference storageImageLoc = mStorageReference.child( postList.get(position).getImageUrl() );
        storageImageLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(postPic);
            }
        });


        /**
         * Saves the current post in the user's database.
         * Will be used when the user checks out their like/favorite.
         * Saves the UID and the category.
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateDatabase(postList.get(position));
                saveButton.setImageResource(R.drawable.liked);

            }
        });




        return convertView;
    }

    /**
     * When a user clicks on one of the Post <3 ,
     * This method is called to save the post in their user data.
     * The save post is later used to display Liked / Favorite Posts.
     * @param post_id
     */
    private void updateDatabase(PostData post_id) {
        // Get current user's data
        DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        ArrayList<Object> newLiked_Posts = new ArrayList<>();

        // Get the current list of like / fav
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    // Begin reading data from firebase and trying to get the current like / fav list.
                    try {

                        newLiked_Posts.add(post_id);
                        for( Object item : (List<Object>) document.get(LIKED_POSTS)) {
                            newLiked_Posts.add(item);
                        }

                        // Update the current list.
                        docRef.update(LIKED_POSTS, newLiked_Posts)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Database successfully updated.");
                                        Toast.makeText(context,  "The Post has been saved to your Likes / Favorite", Toast.LENGTH_SHORT).show();


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                    } catch(Exception e) {
                        Log.d(TAG, "Something went wrong when reading from the database.");
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
