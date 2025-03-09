package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        databaseHelper = new DatabaseHelper(this);

        new Handler().postDelayed(() -> {
            // Check if user exists in the database
            if (databaseHelper.isUserExists()) {
                startActivity(new Intent(SplashActivity.this, FeelingPromptActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, UserinfoActivity.class));
            }
            finish();
        }, 1500); // 2 seconds delay
    }
}
