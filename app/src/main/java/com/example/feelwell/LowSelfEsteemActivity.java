package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class LowSelfEsteemActivity extends AppCompatActivity {

    private int totalScore = 0;
    private int userScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_self_esteem); // Ensure this matches your XML layout file name

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

        // Reverse scoring for negatively worded items
        userScore += getScoreForItem(R.id.radioGroupQ1, false); // Item 1: Reverse score
        userScore += getScoreForItem(R.id.radioGroupQ2, true);  // Item 2: Normal score
        userScore += getScoreForItem(R.id.radioGroupQ3, false); // Item 3: Reverse score
        userScore += getScoreForItem(R.id.radioGroupQ4, false); // Item 4: Reverse score
        userScore += getScoreForItem(R.id.radioGroupQ5, true);  // Item 5: Normal score
        userScore += getScoreForItem(R.id.radioGroupQ6, true);  // Item 6: Normal score
        userScore += getScoreForItem(R.id.radioGroupQ7, false); // Item 7: Reverse score
        userScore += getScoreForItem(R.id.radioGroupQ8, true);  // Item 8: Normal score
        userScore += getScoreForItem(R.id.radioGroupQ9, true);  // Item 9: Normal score
        userScore += getScoreForItem(R.id.radioGroupQ10, false); // Item 10: Reverse score

        totalScore = 40; // Maximum possible score for RSE (10 questions * 4 points each)
    }

    private int getScoreForItem(int radioGroupId, boolean isNormalScoring) {
        RadioGroup group = findViewById(radioGroupId);
        int selectedId = group.getCheckedRadioButtonId();

        if (selectedId == -1) {
            return 0; // No selection
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        int score = Integer.parseInt(selectedRadioButton.getTag().toString());

        // Reverse scoring for negatively worded items
        if (!isNormalScoring) {
            score = 5 - score; // Reverse the score (1 becomes 4, 2 becomes 3, etc.)
        }

        return score;
    }

    private String getSelfEsteemLevel(int score) {
        if (score >= 31) return "Very Low Self-Esteem";
        else if (score >= 21) return "Low Self-Esteem";
        else if (score >= 11) return "Moderate Self-Esteem";
        else if (score >= 6) return "High Self-Esteem";
        else return "Very High Self-Esteem";
    }

    private void openResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("TOTAL_SCORE", totalScore);
        intent.putExtra("USER_SCORE", userScore);
        intent.putExtra("SEVERITY_LEVEL", getSelfEsteemLevel(userScore));
        startActivity(intent);
    }
}