package com.example.feelwell; // Replace with your package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FeelingPromptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_prompt);

        // Initialize buttons
        Button btnDepressed = findViewById(R.id.btnDepressed);
        Button btnStressed = findViewById(R.id.btnStressed);
        Button btnAnxiety = findViewById(R.id.btnAnxiety);
        Button btnAll = findViewById(R.id.btnAll);

        // Set click listeners
        btnDepressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("Depressed");
            }
        });

        btnStressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("Stressed");
            }
        });

        btnAnxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("Anxiety");
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("All of the above");
            }
        });
    }

    private void navigateToMainActivity(String feeling) {
        // Create an Intent to start the MainActivity
        Intent intent = new Intent(FeelingPromptActivity.this, MainActivity.class);

        // Pass the feeling data to the MainActivity
        intent.putExtra("FEELING", feeling);

        // Start the MainActivity
        startActivity(intent);

        // Optional: Finish the FeelingPromptActivity so the user can't go back to it
        finish();
    }
}