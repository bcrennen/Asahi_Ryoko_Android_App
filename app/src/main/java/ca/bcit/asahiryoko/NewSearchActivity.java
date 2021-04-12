package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.auth.User;

public class NewSearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private ImageButton searchBtn;
    private RecyclerView resultView;
    private DatabaseReference databaseReference;
    private FloatingActionButton postFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserUploads");

        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);

        postFab = findViewById(R.id.postFloat);
        searchBar = findViewById(R.id.searchBar);
        searchBtn = findViewById(R.id.searchButton);
        resultView = findViewById(R.id.resultList);
        resultView.setHasFixedSize(true);
        resultView.setLayoutManager(new LinearLayoutManager(this));

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
                        return true;

                    case R.id.like:
                        startActivity(new Intent(getApplicationContext(),
                                LikedAndFavActivity.class));
                        overridePendingTransition(0,0);
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

        postFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postActivityIntent = new Intent(NewSearchActivity.this, UserPostActivity.class);
                startActivity(postActivityIntent);
            }
        });

    }
}
