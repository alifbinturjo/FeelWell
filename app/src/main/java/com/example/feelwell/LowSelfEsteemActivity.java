package com.example.feelwell;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LowSelfEsteemActivity extends AppCompatActivity {

    // Declare RadioGroups for all 10 questions
    private RadioGroup[] questionRadioGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_self_esteem);

        // Initialize RadioGroups for all 10 questions
        questionRadioGroups = new RadioGroup[]{
                findViewById(R.id.question1RadioGroup),
                findViewById(R.id.question2RadioGroup),
                findViewById(R.id.question3RadioGroup),
                findViewById(R.id.question4RadioGroup),
                findViewById(R.id.question5RadioGroup),
                findViewById(R.id.question6RadioGroup),
                findViewById(R.id.question7RadioGroup),
                findViewById(R.id.question8RadioGroup),
                findViewById(R.id.question9RadioGroup),
                findViewById(R.id.question10RadioGroup)
        };

        // Initialize the calculate button
        findViewById(R.id.calculateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSelfEsteemScore();
            }
        });
    }

    private void calculateSelfEsteemScore() {
        int totalScore = 0;

        // Loop through all RadioGroups to get the selected option
        for (int i = 0; i < questionRadioGroups.length; i++) {
            RadioGroup radioGroup = questionRadioGroups[i];
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                // If no option is selected, show an error message
                Toast.makeText(LowSelfEsteemActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                return;
            }

            // Find the index of the selected RadioButton (0 = Strongly Agree, 1 = Agree, etc.)
            RadioButton selectedButton = findViewById(selectedId);
            int index = radioGroup.indexOfChild(selectedButton);

            // Calculate the score based on the question and selected option
            totalScore += getScoreForQuestion(i, index);
        }

        // Interpret the total score
        String result = interpretSelfEsteemScore(totalScore);

        // Display the result in a Toast message
        Toast.makeText(LowSelfEsteemActivity.this, result, Toast.LENGTH_LONG).show();

        // Save the test history to the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get the current date
        String currentDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        // Insert the test history
        dbHelper.insertTestHistory("rses", currentDate, result);
    }

    private int getScoreForQuestion(int questionIndex, int selectedIndex) {
        // For RSES, some questions are positively worded and some are negatively worded.
        // Positively worded questions: 1, 2, 4, 6, 7
        // Negatively worded questions: 3, 5, 8, 9, 10

        // Positively worded questions: Strongly Agree = 3, Agree = 2, Disagree = 1, Strongly Disagree = 0
        // Negatively worded questions: Strongly Agree = 0, Agree = 1, Disagree = 2, Strongly Disagree = 3

        if (questionIndex == 0 || questionIndex == 1 || questionIndex == 3 || questionIndex == 5 || questionIndex == 6) {
            // Positively worded questions
            return 3 - selectedIndex;
        } else {
            // Negatively worded questions
            return selectedIndex;
        }
    }

    private String interpretSelfEsteemScore(int totalScore) {
        if (totalScore >= 30) {
            return "Self-Esteem Level: High\nTotal Score: " + totalScore;
        } else if (totalScore >= 20 && totalScore <= 29) {
            return "Self-Esteem Level: Moderate\nTotal Score: " + totalScore;
        } else if (totalScore >= 10 && totalScore <= 19) {
            return "Self-Esteem Level: Low\nTotal Score: " + totalScore;
        } else if (totalScore >= 0 && totalScore <= 9) {
            return "Self-Esteem Level: Very Low\nTotal Score: " + totalScore;
        } else {
            return "Invalid score";
        }
    }
}