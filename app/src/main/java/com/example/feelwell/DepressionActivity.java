package com.example.feelwell;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DepressionActivity extends AppCompatActivity {

    // Declare RadioGroups for all 9 questions
    private RadioGroup[] questionRadioGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depression);

        // Initialize RadioGroups for all 9 questions
        questionRadioGroups = new RadioGroup[]{
                findViewById(R.id.question1RadioGroup),
                findViewById(R.id.question2RadioGroup),
                findViewById(R.id.question3RadioGroup),
                findViewById(R.id.question4RadioGroup),
                findViewById(R.id.question5RadioGroup),
                findViewById(R.id.question6RadioGroup),
                findViewById(R.id.question7RadioGroup),
                findViewById(R.id.question8RadioGroup),
                findViewById(R.id.question9RadioGroup)
        };

        // Initialize the calculate button
        findViewById(R.id.calculateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDepressionScore();
            }
        });
    }

    private void calculateDepressionScore() {
        int totalScore = 0;

        // Loop through all RadioGroups to get the selected option
        for (RadioGroup radioGroup : questionRadioGroups) {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                // If no option is selected, show an error message
                Toast.makeText(DepressionActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                return;
            }

            // Find the index of the selected RadioButton (0 = Not at all, 1 = Several days, etc.)
            RadioButton selectedButton = findViewById(selectedId);
            int index = radioGroup.indexOfChild(selectedButton);

            // Add the score based on the index
            totalScore += index;
        }

        // Interpret the total score
        String result = interpretDepressionScore(totalScore);

        // Display the result using a Toast message
        // Checking
        Toast.makeText(DepressionActivity.this, result, Toast.LENGTH_LONG).show();
    }

    private String interpretDepressionScore(int totalScore) {
        if (totalScore >= 0 && totalScore <= 4) {
            return "Depression Severity: Minimal depression\nTotal Score: " + totalScore;
        } else if (totalScore >= 5 && totalScore <= 9) {
            return "Depression Severity: Mild depression\nTotal Score: " + totalScore;
        } else if (totalScore >= 10 && totalScore <= 14) {
            return "Depression Severity: Moderate depression\nTotal Score: " + totalScore;
        } else if (totalScore >= 15 && totalScore <= 19) {
            return "Depression Severity: Moderately severe depression\nTotal Score: " + totalScore;
        } else if (totalScore >= 20 && totalScore <= 27) {
            return "Depression Severity: Severe depression\nTotal Score: " + totalScore;
        } else {
            return "Invalid score";
        }
    }
}