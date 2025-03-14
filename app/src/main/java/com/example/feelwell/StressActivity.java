package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class StressActivity extends AppCompatActivity {

    private int totalScore = 0;
    private int userScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress); // Ensure this matches your XML layout file name

        Button calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScore();
                openResultActivity();
            }
        });
    }

    private void calculateScore() {
        userScore = 0;

        // Add scores from all questions
        userScore += getScoreFromGroup(R.id.radioGroupQ1);
        userScore += getScoreFromGroup(R.id.radioGroupQ2);
        userScore += getScoreFromGroup(R.id.radioGroupQ3);
        userScore += getScoreFromGroup(R.id.radioGroupQ4);
        userScore += getScoreFromGroup(R.id.radioGroupQ5);
        userScore += getScoreFromGroup(R.id.radioGroupQ6);
        userScore += getScoreFromGroup(R.id.radioGroupQ7);
        userScore += getScoreFromGroup(R.id.radioGroupQ8);
        userScore += getScoreFromGroup(R.id.radioGroupQ9);
        userScore += getScoreFromGroup(R.id.radioGroupQ10);

        totalScore = 40; // Maximum possible score for PSS (10 questions * 4 points each)
    }

    private int getScoreFromGroup(int radioGroupId) {
        RadioGroup group = findViewById(radioGroupId);
        int selectedId = group.getCheckedRadioButtonId();

        if (selectedId == -1) {
            return 0; // No selection
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        return Integer.parseInt(selectedRadioButton.getTag().toString());
    }

    private String getStressLevel(int score) {
        if (score >= 27) return "High Stress";
        else if (score >= 14) return "Moderate Stress";
        else return "Low Stress";
    }

    private void openResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("TOTAL_SCORE", totalScore);
        intent.putExtra("USER_SCORE", userScore);
        intent.putExtra("SEVERITY_LEVEL", getStressLevel(userScore));
        startActivity(intent);
    }
}