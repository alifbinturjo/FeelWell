package com.example.feelwell; // Replace with your package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the feeling data from the Intent
        Intent intent = getIntent();
        String feeling = intent.getStringExtra("FEELING");

        // Display the feeling in a TextView (or use it as needed)
        TextView feelingTextView = findViewById(R.id.feelingTextView);
        feelingTextView.setText("You're feeling: " + feeling);
    }
}