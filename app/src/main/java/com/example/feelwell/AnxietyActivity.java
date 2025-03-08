package com.example.feelwell;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AnxietyActivity extends AppCompatActivity {

    // Declare RadioGroups for all 7 questions
    private RadioGroup[] questionRadioGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anxiety);

        // Initialize RadioGroups for all 7 questions
        questionRadioGroups = new RadioGroup[]{
                findViewById(R.id.question1RadioGroup),
                findViewById(R.id.question2RadioGroup),
                findViewById(R.id.question3RadioGroup),
                findViewById(R.id.question4RadioGroup),
                findViewById(R.id.question5RadioGroup),
                findViewById(R.id.question6RadioGroup),
                findViewById(R.id.question7RadioGroup)
        };

        // Initialize the calculate button
        findViewById(R.id.calculateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAnxietyScore();
            }
        });
    }

    private void calculateAnxietyScore() {
        int totalScore = 0;

        // Loop through all RadioGroups to get the selected option
        for (RadioGroup radioGroup : questionRadioGroups) {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                // If no option is selected, show an error message
                Toast.makeText(AnxietyActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                return;
            }

            // Find the index of the selected RadioButton (0 = Not at all, 1 = Several days, etc.)
            RadioButton selectedButton = findViewById(selectedId);
            int index = radioGroup.indexOfChild(selectedButton);

            // Add the score based on the index
            totalScore += index;
        }

        // Interpret the total score
        String result = interpretAnxietyScore(totalScore);

        // Display the result using a Toast message
        Toast.makeText(AnxietyActivity.this, result, Toast.LENGTH_LONG).show();

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get the current date
        String currentDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        // Insert the test history
        dbHelper.insertTestHistory("gad7", currentDate, result);
    }

    private String interpretAnxietyScore(int totalScore) {
        if (totalScore >= 0 && totalScore <= 4) {
            return "Anxiety Severity: Minimal anxiety\nTotal Score: " + totalScore;
        } else if (totalScore >= 5 && totalScore <= 9) {
            return "Anxiety Severity: Mild anxiety\nTotal Score: " + totalScore;
        } else if (totalScore >= 10 && totalScore <= 14) {
            return "Anxiety Severity: Moderate anxiety\nTotal Score: " + totalScore;
        } else if (totalScore >= 15 && totalScore <= 21) {
            return "Anxiety Severity: Severe anxiety\nTotal Score: " + totalScore;
        } else {
            return "Invalid score";
        }
    }
}
