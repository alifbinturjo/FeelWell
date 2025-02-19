package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the feeling data from the Intent
        Intent intent = getIntent();
        String feeling = intent.getStringExtra("FEELING");

        // Display the feeling in a TextView
        TextView feelingTextView = findViewById(R.id.feelingTextView);
        if (feeling != null) {
            feelingTextView.setText("You're feeling: " + feeling);
        }

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Get the menu and add items dynamically (without icons)
        Menu menu = bottomNavigationView.getMenu();
        menu.add(0, 1, 0, "Assessment");
        menu.add(0, 2, 1, "Task");
        menu.add(0, 3, 2, "Profile");

        // Handle menu item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        // TODO: Implement Assessment Screen
                        return true;
                    case 2:
                        // TODO: Implement Task Screen
                        return true;
                    case 3:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
}
