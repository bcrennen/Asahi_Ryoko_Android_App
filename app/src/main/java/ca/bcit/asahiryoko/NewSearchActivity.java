package ca.bcit.asahiryoko;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewSearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private ImageButton searchBtn;
    private RecyclerView resultView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserUploads");

        searchBar = findViewById(R.id.searchBar);
        searchBtn = findViewById(R.id.searchButton);
        resultView = findViewById(R.id.resultList);
        resultView.setHasFixedSize(true);
        resultView.setLayoutManager(new LinearLayoutManager(this));
        
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
