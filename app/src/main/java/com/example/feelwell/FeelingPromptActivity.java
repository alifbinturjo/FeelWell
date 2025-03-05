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
        Button btnAnxiety = findViewById(R.id.btnAnxiety);
        Button btnStressed = findViewById(R.id.btnStressed);
        Button btnLowSelfEsteem = findViewById(R.id.btnLowSelfEsteem);
        Button btnAll = findViewById(R.id.btnAll);


        // Set click listeners
        btnDepressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("Depression");
            }
        });

        btnAnxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("Anxiety");
            }
        });

        btnStressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("Stress");
            }
        });

        btnLowSelfEsteem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity("Low Self-Esteem");
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If "All of the Above" is selected, pass Depression, Anxiety, and Stress
                navigateToMainActivity("Depression, Anxiety, Stress");
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
