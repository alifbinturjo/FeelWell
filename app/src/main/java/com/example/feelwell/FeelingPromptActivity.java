package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Set click listeners for each card

        depressionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass feeling to MainActivity
                navigateToMainActivity("Depression");
            }
        });

        anxietyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass feeling to MainActivity
                navigateToMainActivity("Anxiety");
            }
        });

        stressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass feeling to MainActivity
                navigateToMainActivity("Stress");
            }
        });

        selfEsteemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass feeling to MainActivity
                navigateToMainActivity("Low Self-Esteem");
            }
        });
    }

    // Helper method to navigate to MainActivity with the feeling data
    private void navigateToMainActivity(String feeling) {
        Intent intent = new Intent(FeelingPromptActivity.this, MainActivity.class);
        intent.putExtra("FEELING", feeling);  // Pass the selected feeling
        startActivity(intent);
    }
}
