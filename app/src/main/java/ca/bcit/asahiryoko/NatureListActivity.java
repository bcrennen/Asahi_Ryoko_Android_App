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

/**
 * This is a testing class for reading the data.
 *
 * Author: Brennen Chiu
 * Date: April 11th 2021
 * Version: 1.0
 * */
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

        mDatabaseReference.child("Nature").child("-MY1FvPc8CbkVexp70ww").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot document = task.getResult();
                    StorageReference storageImageLoc = mStorageReference.child((document.getValue(PostData.class).getImageUrl()));
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