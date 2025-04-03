package com.example.feelwell;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeActivity extends AppCompatActivity {
    private TextView taskDescription;
    private TextView timerText;
    private Button startButton, pauseButton, resetButton;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private long totalTimeInMillis;
    private boolean timerRunning;
    private int durationMinutes;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        // Initialize views
        taskDescription = findViewById(R.id.taskDescription);
        timerText = findViewById(R.id.timerText);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);

        // Setup button colors
        startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        pauseButton.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        resetButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

        // Get task description
        description = getIntent().getStringExtra("task_description");
        taskDescription.setText(description != null ? description : "Activity Time");

        // Extract and set timer duration
        durationMinutes = extractTimeFromDescription(description);
        totalTimeInMillis = durationMinutes * 60 * 1000;
        timeLeftInMillis = totalTimeInMillis;
        updateCountDownText();
        progressBar.setProgress(100);

        // Button click listeners
        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());

        updateButtonStates();
    }

    private int extractTimeFromDescription(String description) {
        if (description == null) return 5;
        Pattern pattern = Pattern.compile("(\\d+)\\s+minute");
        Matcher matcher = pattern.matcher(description.toLowerCase());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 5;
    }

    private void startTimer() {
        if (!timerRunning) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 100) { // 100ms for smoother updates
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                    updateProgressBar();
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    timeLeftInMillis = 0;
                    updateCountDownText();
                    progressBar.setProgress(0);
                    progressBar.getProgressDrawable().setColorFilter(
                            ContextCompat.getColor(TimeActivity.this, R.color.green), PorterDuff.Mode.SRC_IN);
                    showCompletionDialog();
                }
            }.start();

            timerRunning = true;
            updateButtonStates();
        }
    }

    private void pauseTimer() {
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
            updateButtonStates();
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = totalTimeInMillis;
        updateCountDownText();
        progressBar.setProgress(100);
        progressBar.getProgressDrawable().clearColorFilter();
        timerRunning = false;
        updateButtonStates();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerText.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void updateProgressBar() {
        int progress = (int) ((timeLeftInMillis * 100) / totalTimeInMillis);
        progressBar.setProgress(progress);

        // Update progress color based on remaining time
        if (progress > 50) {
            progressBar.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.blue), PorterDuff.Mode.SRC_IN);
        } else if (progress > 25) {
            progressBar.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.orange), PorterDuff.Mode.SRC_IN);
        } else {
            progressBar.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_IN);
        }
    }

    private void updateButtonStates() {
        startButton.setVisibility(timerRunning ? View.GONE : View.VISIBLE);
        pauseButton.setVisibility(timerRunning ? View.VISIBLE : View.GONE);
        resetButton.setEnabled(!timerRunning);
    }

    private void showCompletionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Task Completed!")
                .setMessage("Great job completing your " + durationMinutes + " minute activity!")
                .setPositiveButton("Done", (dialog, which) -> {
                    setCompletionResult(true);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void setCompletionResult(boolean isCompleted) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_completed", isCompleted);
        resultIntent.putExtra("task_name", description);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        setCompletionResult(false);
        super.onBackPressed();
    }
}