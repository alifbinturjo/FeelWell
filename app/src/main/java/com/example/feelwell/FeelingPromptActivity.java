package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FeelingPromptActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String testName;

    // Constants for test names
    private static final String TEST_PHQ9 = "phq9";
    private static final String TEST_PSS = "pss";
    private static final String TEST_GAD7 = "gad7";
    private static final String TEST_RSE = "rse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_prompt);

        dbHelper = new DatabaseHelper(this);

        RelativeLayout depressionCard = findViewById(R.id.depressionCard);
        RelativeLayout anxietyCard = findViewById(R.id.anxietyCard);
        RelativeLayout stressCard = findViewById(R.id.stressCard);
        RelativeLayout selfEsteemCard = findViewById(R.id.selfEsteemCard);

        ImageButton profileButton = findViewById(R.id.profileButton);

        // Initialize progress views
        CircularProgressIndicator progressBar = findViewById(R.id.progressBar);
        TextView progressText = findViewById(R.id.progressText);

        // Set up task progress
        int totalTasks = dbHelper.getTotalAssignedTasksCount();
        int completedTasks = dbHelper.getCompletedTasksCount();
        int progress = 0;
        if (totalTasks > 0) {
            progress = (int) (((float) completedTasks / totalTasks) * 100);
        }
        progressText.setText(String.format("Task Progress: %d%%", progress));
        progressBar.setProgress(progress);

        depressionCard.setOnClickListener(v -> checkAndNavigate("Depression"));
        anxietyCard.setOnClickListener(v -> checkAndNavigate("Anxiety"));
        stressCard.setOnClickListener(v -> checkAndNavigate("Stress"));
        selfEsteemCard.setOnClickListener(v -> checkAndNavigate("Low Self-Esteem"));

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeelingPromptActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Make brain image clickable to navigate to tasks
        findViewById(R.id.brainImage).setOnClickListener(v -> {
            Intent intent = new Intent(FeelingPromptActivity.this, TaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        CircularProgressIndicator progressBar = findViewById(R.id.progressBar);
        TextView progressText = findViewById(R.id.progressText);

        int totalTasks = dbHelper.getTotalAssignedTasksCount();
        int completedTasks = dbHelper.getCompletedTasksCount();
        int progress = 0;
        if (totalTasks > 0) {
            progress = (int) (((float) completedTasks / totalTasks) * 100);
        }

        progressText.setText(String.format("Task Progress: %d%%", progress));
        progressBar.setProgress(progress);
    }


    private void checkAndNavigate(String feeling) {
        switch (feeling) {
            case "Depression":
                testName = TEST_PHQ9;
                break;
            case "Stress":
                testName = TEST_PSS;
                break;
            case "Anxiety":
                testName = TEST_GAD7;
                break;
            case "Low Self-Esteem":
                testName = TEST_RSE;
                break;
            default:
                testName = "";
                break;
        }

        String lastTestDate = dbHelper.getLastTestDate(testName);

        if (lastTestDate == null) {
            navigateToMainActivity(feeling);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date lastDate = dateFormat.parse(lastTestDate);
                Date today = Calendar.getInstance().getTime();

                long diffInMillis = today.getTime() - lastDate.getTime();
                long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

                int lockPeriodDays;
                if ("Depression".equals(feeling) || "Anxiety".equals(feeling)) {
                    lockPeriodDays = 14; // Lock for 14 days for Depression and Anxiety
                } else if ("Stress".equals(feeling)) {
                    lockPeriodDays = 30; // Lock for 30 days for Stress
                } else {
                    lockPeriodDays = 1; // Lock for 1 day for Self-Esteem
                }

                if (diffInDays < lockPeriodDays) {
                    long remainingDays = lockPeriodDays - diffInDays;
                    Toast.makeText(this, "Your assessment will be unlocked in " + remainingDays + " days.", Toast.LENGTH_LONG).show();
                } else {
                    navigateToMainActivity(feeling);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                navigateToMainActivity(feeling); // Fallback in case of error
            }
        }
    }

    private void navigateToMainActivity(String feeling) {
        Intent intent = new Intent(FeelingPromptActivity.this, MainActivity.class);
        intent.putExtra("FEELING", feeling);
        startActivity(intent);
    }
}