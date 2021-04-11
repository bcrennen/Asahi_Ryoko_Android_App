package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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


public class NatureListActivity extends AppCompatActivity {

    private ImageView testPhotoView;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nature_list);

        testPhotoView = findViewById(R.id.Testphoto);

        mStorageReference = FirebaseStorage.getInstance().getReference("Uploads");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UserUploads");

        mDatabaseReference.child("Landmarks").child("-MY-cqJkdp1rGbbuX1C0").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot document = task.getResult();
                    StorageReference storageImageLoc = mStorageReference.child("Uploads").child((document.getValue(PostData.class).getImageUrl() + ".jpg"));
                    storageImageLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(NatureListActivity.this).load(uri).into(testPhotoView);
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