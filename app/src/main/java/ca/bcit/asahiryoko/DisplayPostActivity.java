package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayPostActivity extends AppCompatActivity {

    private final String TAG = "DisplayPostActivity";
    private ListView postListView;
    ArrayList<PostData> postDataList = new ArrayList<PostData>();
    ArrayList<String> names = new ArrayList<String>();

    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("Uploads");
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("UserUploads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

        postListView = findViewById(R.id.postDisplayList);
        //TODO: TEMP String Filter, replace with Intent Extra
        String intentFilter = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intentFilter = extras.getString("Filter");
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(intentFilter);

        getPostFromDB(intentFilter, new GetPostArrayData() {

            @Override
            public void dataLoaded(ArrayList<PostData> responsePostData) {

                PostAdapter adapter = new PostAdapter(DisplayPostActivity.this, android.R.layout.simple_list_item_1, responsePostData);
                postListView.setAdapter(adapter);
            }
        });


    }

    private void getPostFromDB(String filter, GetPostArrayData post_data) {
        // Get a list of Post from one of the selected options.
        DatabaseReference filterDBRef = mDatabaseReference.child(filter);

        filterDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot : snapshot.getChildren()) {
                    postDataList.add(datasnapshot.getValue(PostData.class));
                }
                post_data.dataLoaded(postDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface GetPostArrayData {
        void dataLoaded(ArrayList<PostData> postDataList);
    }
}