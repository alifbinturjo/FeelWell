package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class DepressionActivity extends AppCompatActivity {

    private int totalScore = 0;
    private int userScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depression);

        Button endTestButton = findViewById(R.id.endTestButton);

        endTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScore();
                openResultActivity();
            }
        });
    }

    private void calculateScore() {
        userScore = 0;

        userScore += getScoreFromGroup(R.id.radioGroupQ1);
        userScore += getScoreFromGroup(R.id.radioGroupQ2);
        userScore += getScoreFromGroup(R.id.radioGroupQ3);
        userScore += getScoreFromGroup(R.id.radioGroupQ4);
        userScore += getScoreFromGroup(R.id.radioGroupQ5);
        userScore += getScoreFromGroup(R.id.radioGroupQ6);
        userScore += getScoreFromGroup(R.id.radioGroupQ7);
        userScore += getScoreFromGroup(R.id.radioGroupQ8);
        userScore += getScoreFromGroup(R.id.radioGroupQ9);

        totalScore = 27; // Maximum possible score for PHQ-9
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

    private String getSeverityLevel(int score) {
        if (score >= 20) return "Severe Depression";
        else if (score >= 15) return "Moderately Severe Depression";
        else if (score >= 10) return "Moderate Depression";
        else if (score >= 5) return "Mild Depression";
        else return "Minimal or No Depression";
    }

    private void openResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("TOTAL_SCORE", totalScore);
        intent.putExtra("USER_SCORE", userScore);
        intent.putExtra("SEVERITY_LEVEL", getSeverityLevel(userScore));
        startActivity(intent);
    }
}