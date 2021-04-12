package ca.bcit.asahiryoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
/***
 * This Activity initiates the app and is the Main activity/Homepage.
 * Author: Kevin Lee, Saksham Bhardwaj, Brennen Chiu
 * Data April 09, 2021
 * Version: 1.0
 */
public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private ImageButton postButton;

    private FloatingActionButton postFab;

    RelativeLayout rellay_nature, rellay_landmark, rellay_food, rellay_market, rellay_leisure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.LogoutBtn);

        postFab = findViewById(R.id.postFloat);
	/***
	References - https://developer.android.com/guide/topics/ui/controls/button			    				    https://www.youtube.com/watch?v=JjfSjMs0ImQ	
	*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),
                                NewSearchActivity.class));
                        overridePendingTransition(0,0);
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

        rellay_nature = findViewById(R.id.nature);
        rellay_landmark = findViewById(R.id.landmark);
        rellay_food = findViewById(R.id.food);
        rellay_market = findViewById(R.id.market);
        rellay_leisure = findViewById(R.id.leisure);

        rellay_nature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPostActivity.class);
                intent.putExtra("Filter", "Nature");
                startActivity(intent);
            }
        });

        rellay_landmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPostActivity.class);
                intent.putExtra("Filter", "Landmarks");
                startActivity(intent);
            }
        });
        rellay_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPostActivity.class);
                intent.putExtra("Filter", "Food");
                startActivity(intent);
            }
        });
        rellay_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPostActivity.class);
                intent.putExtra("Filter", "Market Or Shopping");
                startActivity(intent);
            }
        });
        rellay_leisure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPostActivity.class);
                intent.putExtra("Filter", "Leisure");
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class));
                finish();
            }
        });

        /***
         * This button is to take the user to post activity to post their experiences.
         *
         * Author: Brennen Chiu
         * Date: April 11, 2021
         * Version: 1.0
         */
        postFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postActivityIntent = new Intent(MainActivity.this, UserPostActivity.class);
                startActivity(postActivityIntent);
            }
        });
    }
}