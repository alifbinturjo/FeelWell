package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnxietyActivity extends AppCompatActivity {

    private int totalScore = 0;
    private int userScore = 0;
    private DatabaseHelper dbHelper; // Database helper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anxiety); // Ensure this matches your XML layout file name

        dbHelper = new DatabaseHelper(this); // Initialize database helper

        Button calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScore();
                saveTestHistory(); // Insert the result into the database and assign tasks
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

        totalScore = 21; // Maximum possible score for GAD-7 (7 questions * 3 points each)
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

    private String getAnxietyLevel(int score) {
        if (score >= 15) return "Severe Anxiety";
        else if (score >= 10) return "Moderate Anxiety";
        else if (score >= 5) return "Mild Anxiety";
        else return "Minimal or No Anxiety";
    }

    private void saveTestHistory() {
        String testName = "gad7";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String anxietyLevel = getAnxietyLevel(userScore);

        // Save test result
        dbHelper.insertTestHistory(testName, currentDate, userScore);

        // Assign tasks - first delete old ones, then assign new ones
        dbHelper.deleteTasksForTest(testName);
        dbHelper.assignTasksForTest(testName, anxietyLevel);
    }

    private void openResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("TOTAL_SCORE", totalScore);
        intent.putExtra("USER_SCORE", userScore);
        intent.putExtra("SEVERITY_LEVEL", getAnxietyLevel(userScore));
        startActivity(intent);
        finish();
    }
}