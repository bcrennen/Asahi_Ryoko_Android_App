package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
/***
 * This class is allow user to post their their experience
 * with a certain place they visited.
 *
 * Author: Brennen Chiu
 * Date: April 10, 2021
 * Version: 1.0
 * Sources: https://firebase.google.com/docs/storage/web/upload-files
 * Sources: https://www.youtube.com/watch?v=CQ5qcJetYAI&ab_channel=BenO%27Brien
 */
public class UserPostActivity extends AppCompatActivity {

    private static final int PICK_Image_REQUEST = 1;

    private Button choosePhotoBtn;
    private Spinner category;
    private EditText placeName;
    private ImageView userPhoto;
    private EditText placeDescription;
    private ProgressBar progressBar;
    private Button uploadBtn;

    private Uri mImageUri;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        choosePhotoBtn = findViewById(R.id.ChoosePhotoButton);
        category = findViewById(R.id.Category);
        placeName = findViewById(R.id.PlaceName);
        userPhoto = findViewById(R.id.UserPhoto);
        placeDescription = findViewById(R.id.PlaceDescription);
        progressBar = findViewById(R.id.progressBar);
        uploadBtn = findViewById(R.id.UploadButton);

        // will store the image in the folder called Uploads
        mStorageReference = FirebaseStorage.getInstance().getReference("Uploads");
        if (category.getSelectedItem().toString() == "Food") {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Food");
        } else if (category.getSelectedItem().toString() == "Nature") {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Nature");
        } else if (category.getSelectedItem().toString() == "Landmarks") {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Landmarks");
        } else if (category.getSelectedItem().toString() == "Market Or Shopping") {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Market Or Shopping");
        } else if (category.getSelectedItem().toString() == "Leisure") {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Leisure");
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UserUploads");

        // when user click on choose button it will open a file location for them to upload.
        choosePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // this button serves as button for user to upload into database.
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(UserPostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    /***
     * This method here is to create a file opener for the user.
     *
     * Author: Brennen Chiu
     * Date: April 10, 2021
     * Version: 1.0
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_Image_REQUEST);
    }

    /***
     * This method here is to upload the data of the image to firebase and then
     * put it in the box where post is.
     *
     * Author: Brennen Chiu
     * Date: April 10, 2021
     * Version: 1.0
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_Image_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(userPhoto);
        }
    }

    /***
     * This method here is to get the file extension from the file that it is uploaded.
     *
     * Author: Brennen Chiu
     * Date: April 10, 2021
     * Version: 1.0
     */
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /***
     * This method is to upload the file and data that the user has input into
     * firebase database.
     *
     * Author: Brennen Chiu
     * Date: April 10, 2021
     * Version: 1.0
     */
    private void uploadFile() {
        if (mImageUri != null) {
            long fileName = System.currentTimeMillis();
            String nameFile = Long.toString(fileName) + "." + getFileExtension(mImageUri);
            StorageReference photoReference = mStorageReference.child(nameFile);
            uploadTask = photoReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(UserPostActivity.this, "Upload Success!", Toast.LENGTH_LONG).show();
                            String id = mDatabaseReference.push().getKey();
                            UploadImage uploadImage = new UploadImage(
                                    id,
                                    placeName.getText().toString().trim(),
                                    nameFile,
                                    placeDescription.getText().toString().trim());
                            // here is to set up the category as one child and have each post as unique id
                            mDatabaseReference.child(category.getSelectedItem().toString()).child(id).setValue(uploadImage);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No photo selected", Toast.LENGTH_SHORT).show();
        }
    }
}