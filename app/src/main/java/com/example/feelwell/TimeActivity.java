package com.example.feelwell;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TimeActivity extends AppCompatActivity {

    private TextView taskDescription;
    private TextView timerText;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private ImageView timerVisual;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private int durationMinutes;

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
        timerVisual = findViewById(R.id.timerVisual);

        // Get task description from intent
        String description = getIntent().getStringExtra("task_description");
        taskDescription.setText(description);

        // Extract time from description (e.g., "5 minutes" → 5)
        durationMinutes = extractTimeFromDescription(description);
        timeLeftInMillis = durationMinutes * 60 * 1000; // Convert to milliseconds

        updateCountDownText();
        setupButtons();
    }

    private int extractTimeFromDescription(String description) {
        // Pattern to match numbers followed by "minute" or "minutes"
        Pattern pattern = Pattern.compile("(\\d+)\\s+minute");
        Matcher matcher = pattern.matcher(description.toLowerCase());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 5; // Default value if no time found
    }

    private void setupButtons() {
        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        if (!timerRunning) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                    updateTimerVisual();
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    timerText.setText("00:00");
                    timerVisual.setRotation(0);
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
        timeLeftInMillis = durationMinutes * 60 * 1000;
        updateCountDownText();
        timerVisual.setRotation(0);
        timerRunning = false;
        updateButtonStates();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(timeLeftFormatted);
    }

    private void updateTimerVisual() {
        float progress = (float) timeLeftInMillis / (durationMinutes * 60 * 1000);
        float rotation = 360 * (1 - progress);
        timerVisual.setRotation(rotation);
    }

    private void updateButtonStates() {
        startButton.setVisibility(timerRunning ? View.GONE : View.VISIBLE);
        pauseButton.setVisibility(timerRunning ? View.VISIBLE : View.GONE);
    }

    private void showCompletionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Task Completed!")
                .setMessage("Great job completing your " + durationMinutes + " minute activity!")
                .setPositiveButton("Done", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}