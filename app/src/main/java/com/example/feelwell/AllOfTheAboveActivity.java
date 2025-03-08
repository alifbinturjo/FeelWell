package com.example.feelwell;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AllOfTheAboveActivity extends AppCompatActivity {

    private RadioGroup[] questionRadioGroups = new RadioGroup[21]; // Array to hold all RadioGroups
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_of_the_above);

        // Initialize RadioGroups for all questions
        for (int i = 0; i < 21; i++) {
            String radioGroupID = "question" + (i + 1) + "RadioGroup";
            int resID = getResources().getIdentifier(radioGroupID, "id", getPackageName());
            questionRadioGroups[i] = findViewById(resID);
        }

        // Initialize Calculate Button
        calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScores();
            }
        });
    }

    private void calculateScores() {
        int depressionScore = 0;
        int anxietyScore = 0;
        int stressScore = 0;

        int[] depressionIndexes = {2, 4, 9, 12, 15, 16, 20}; // Depression question indexes
        int[] anxietyIndexes = {1, 3, 6, 8, 14, 18, 19}; // Anxiety question indexes
        int[] stressIndexes = {0, 5, 7, 10, 11, 13, 17}; // Stress question indexes

        for (int i = 0; i < questionRadioGroups.length; i++) {
            int selectedRadioButtonId = questionRadioGroups[i].getCheckedRadioButtonId();

            if (selectedRadioButtonId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                int score = getScoreFromRadioButton(selectedRadioButton);

                if (contains(depressionIndexes, i)) {
                    depressionScore += score;
                } else if (contains(anxietyIndexes, i)) {
                    anxietyScore += score;
                } else if (contains(stressIndexes, i)) {
                    stressScore += score;
                }
            } else {
                Toast.makeText(this, "Please answer all questions.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Multiply each score by 2 to align with the full DASS-42 scale
        depressionScore *= 2;
        anxietyScore *= 2;
        stressScore *= 2;

        // Interpret the scores
        String resultMessage = "Depression Score: " + depressionScore + " (" + interpretDepression(depressionScore) + ")\n"
                + "Anxiety Score: " + anxietyScore + " (" + interpretAnxiety(anxietyScore) + ")\n"
                + "Stress Score: " + stressScore + " (" + interpretStress(stressScore) + ")";

        // Display results in a Toast
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();
    }

    private int getScoreFromRadioButton(RadioButton radioButton) {
        int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
        return index; // 0 = Did not apply, 1 = Some degree, 2 = Considerable degree, 3 = Most of the time
    }

    private boolean contains(int[] array, int value) {
        for (int item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }

    private String interpretDepression(int score) {
        if (score <= 9) return "Normal";
        else if (score <= 12) return "Mild";
        else if (score <= 20) return "Moderate";
        else if (score <= 27) return "Severe";
        else return "Extremely Severe";
    }

    private String interpretAnxiety(int score) {
        if (score <= 6) return "Normal";
        else if (score <= 9) return "Mild";
        else if (score <= 14) return "Moderate";
        else if (score <= 19) return "Severe";
        else return "Extremely Severe";
    }

    private String interpretStress(int score) {
        if (score <= 10) return "Normal";
        else if (score <= 18) return "Mild";
        else if (score <= 26) return "Moderate";
        else if (score <= 34) return "Severe";
        else return "Extremely Severe";
    }
}
