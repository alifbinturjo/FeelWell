package com.example.feelwell;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StressActivity extends AppCompatActivity {

    // Declare RadioGroups for all 10 questions
    private RadioGroup[] stressQuestionRadioGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress);

        // Initialize RadioGroups for all 10 questions
        stressQuestionRadioGroups = new RadioGroup[]{
                findViewById(R.id.stressQuestion1RadioGroup),
                findViewById(R.id.stressQuestion2RadioGroup),
                findViewById(R.id.stressQuestion3RadioGroup),
                findViewById(R.id.stressQuestion4RadioGroup),
                findViewById(R.id.stressQuestion5RadioGroup),
                findViewById(R.id.stressQuestion6RadioGroup),
                findViewById(R.id.stressQuestion7RadioGroup),
                findViewById(R.id.stressQuestion8RadioGroup),
                findViewById(R.id.stressQuestion9RadioGroup),
                findViewById(R.id.stressQuestion10RadioGroup)
        };

        // Initialize the calculate button
        findViewById(R.id.stressCalculateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateStressScore();
            }
        });
    }

    private void calculateStressScore() {
        int totalScore = 0;

        // Loop through all RadioGroups to get the selected option
        for (RadioGroup radioGroup : stressQuestionRadioGroups) {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                // If no option is selected, show an error message
                Toast.makeText(StressActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                return;
            }

            // Find the index of the selected RadioButton (0 = Never, 1 = Almost Never, etc.)
            RadioButton selectedButton = findViewById(selectedId);
            int index = radioGroup.indexOfChild(selectedButton);

            // Add the score based on the index
            totalScore += index;
        }

        // Interpret the total score
        String result = interpretStressScore(totalScore);

        // Display the result using a Toast message
        Toast.makeText(StressActivity.this, result, Toast.LENGTH_LONG).show();
    }

    private String interpretStressScore(int totalScore) {
        if (totalScore >= 0 && totalScore <= 13) {
            return "Stress Level: Low\nTotal Score: " + totalScore;
        } else if (totalScore >= 14 && totalScore <= 26) {
            return "Stress Level: Moderate\nTotal Score: " + totalScore;
        } else if (totalScore >= 27 && totalScore <= 40) {
            return "Stress Level: High\nTotal Score: " + totalScore;
        } else {
            return "Invalid score";
        }
    }
}