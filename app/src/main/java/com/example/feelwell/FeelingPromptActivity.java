package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class FeelingPromptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_prompt);

        // Find the RelativeLayouts (cards) by their IDs
        RelativeLayout depressionCard = findViewById(R.id.depressionCard);
        RelativeLayout anxietyCard = findViewById(R.id.anxietyCard);
        RelativeLayout stressCard = findViewById(R.id.stressCard);
        RelativeLayout selfEsteemCard = findViewById(R.id.selfEsteemCard);

        // Find the profile and task buttons
        ImageButton profileButton = findViewById(R.id.profileButton);
        ImageButton taskButton = findViewById(R.id.taskButton);

        // Set click listeners for each card
        depressionCard.setOnClickListener(v -> navigateToMainActivity("Depression"));
        anxietyCard.setOnClickListener(v -> navigateToMainActivity("Anxiety"));
        stressCard.setOnClickListener(v -> navigateToMainActivity("Stress"));
        selfEsteemCard.setOnClickListener(v -> navigateToMainActivity("Low Self-Esteem"));

        // Navigate to ProfileActivity when profile button is clicked
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeelingPromptActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Navigate to TaskActivity when task button is clicked
        taskButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeelingPromptActivity.this, TaskActivity.class);
            startActivity(intent);
        });
    }

    // Helper method to navigate to MainActivity with the feeling data
    private void navigateToMainActivity(String feeling) {
        Intent intent = new Intent(FeelingPromptActivity.this, MainActivity.class);
        intent.putExtra("FEELING", feeling);  // Pass the selected feeling
        startActivity(intent);
    }
}
