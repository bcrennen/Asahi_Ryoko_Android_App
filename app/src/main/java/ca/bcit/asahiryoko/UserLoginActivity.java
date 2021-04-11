package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UserLoginActivity extends AppCompatActivity {

    /** Used in logging messages and debugging */
    private static final String TAG = "UserLoginActivity";
    private EditText userEmail;
    private EditText userPassword;
    private Button loginButton;
    private Button registerButton;
    private final String DEFAULT_IMAGE = "default_profile.jpg";


    FirebaseAuth firebaseAuth;

    /**
     * Get an instance of the Cloud Firestore.
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Create a Cloud Storage reference from the app
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        userEmail = findViewById(R.id.UserEmail);
        userPassword = findViewById(R.id.UserPassword);
        loginButton = findViewById(R.id.LoginBtn);
        registerButton = findViewById(R.id.RegisterBtn);

        firebaseAuth = FirebaseAuth.getInstance();



        if (firebaseAuth.getCurrentUser() != null) {
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));

            // FIXME: For testing, remove this line later.
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        }

        // Register button add listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailRegister = userEmail.getText().toString().trim();
                String passwordRegister = userPassword.getText().toString().trim();

                if (TextUtils.isEmpty(emailRegister)) {
                    userEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(passwordRegister)) {
                    userPassword.setError("Password is required");
                    return;
                }
                if (passwordRegister.length() < 8) {
                    userPassword.setError("Password must be bigger than 8 characters long.");
                    return;
                }

                // Register user
                firebaseAuth.createUserWithEmailAndPassword(emailRegister, passwordRegister).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserLoginActivity.this, "Welcome!", Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            // FIXME: For testing, remove this line later.
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));


                            create_profile();

                        } else {
                            Toast.makeText(UserLoginActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail = userEmail.getText().toString().trim();
                String loginPassword = userPassword.getText().toString().trim();

                if (TextUtils.isEmpty(loginEmail)) {
                    userEmail.setError("Email required");
                    return;
                }

                if (TextUtils.isEmpty(loginPassword)) {
                    userPassword.setError("Password required");
                    return;
                }

                if (loginPassword.length() < 8) {
                    userPassword.setError("Password must be bigger than 8 characters long");
                    return;
                }

                // authenticate user
                firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserLoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            // FIXME: For testing, remove this line later.
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            Toast.makeText(UserLoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /***
     * This method creates a user profile when the user creates a new account.
     * Stores the profile in the firebase database.
     * Author: Kevin Lee
     * Date: April 09, 2021
     * Version: 1.0
     */
    public void create_profile() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String username = currentUser.getEmail();
        username = username.split("@")[0];

        // Create a new user with a username, profile picture and bio
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("profilePicture", DEFAULT_IMAGE);
        user.put("bio", "No current bio");

        // Add a new document with a generated ID
        db.collection("users").document(firebaseAuth.getUid()).set(user);
    }
}