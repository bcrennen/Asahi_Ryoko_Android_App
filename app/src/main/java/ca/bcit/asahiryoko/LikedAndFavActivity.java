package ca.bcit.asahiryoko;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
/***
 * This Activity is used to display the user's favorite post.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class LikedAndFavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Liked / Favorite");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_and_fav);
    }
}