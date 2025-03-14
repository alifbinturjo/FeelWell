package com.example.feelwell;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private View verticalLine;
    private View userScoreIndicator;
    private TextView severityLevelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        verticalLine = findViewById(R.id.verticalLine);
        userScoreIndicator = findViewById(R.id.userScoreIndicator);
        severityLevelText = findViewById(R.id.severityLevelText);

        // Get data from intent
        Intent intent = getIntent();
        int totalScore = intent.getIntExtra("TOTAL_SCORE", 27);
        int userScore = intent.getIntExtra("USER_SCORE", 0);
        String severityLevel = intent.getStringExtra("SEVERITY_LEVEL");

        // Set severity level text
        severityLevelText.setText("Severity Level: " + severityLevel);

        // Wait for the layout to be fully drawn before positioning the indicator
        verticalLine.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to prevent multiple calls
                verticalLine.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Plot the user's score on the vertical line
                plotUserScore(totalScore, userScore);
            }
        });
    }

    private void plotUserScore(int totalScore, int userScore) {
        // Get the height of the vertical line
        int verticalLineHeight = verticalLine.getHeight();

        // Calculate the position of the user's score indicator
        float userScorePosition = ((float) userScore / totalScore) * verticalLineHeight;

        // Set the position of the user's score indicator
        userScoreIndicator.setY(verticalLine.getY() + verticalLineHeight - userScorePosition);

        // Customize the user's score indicator
        userScoreIndicator.setBackgroundColor(Color.RED);
    }
}