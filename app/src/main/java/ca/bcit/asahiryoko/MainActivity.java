package ca.bcit.asahiryoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private ImageButton postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.LogoutBtn);
        postButton = findViewById(R.id.PostBtn);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class));
                finish();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postActivityIntent = new Intent(MainActivity.this, UserPostActivity.class);
                startActivity(postActivityIntent);
            }
        });
    }

    public void testnaturelist(View view) {
        Intent intent = new Intent(MainActivity.this, NatureListActivity.class);
        startActivity(intent);
    }
}