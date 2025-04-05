package com.example.feelwell;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameText, genderText, ageText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        nameText = findViewById(R.id.nameText);
        genderText = findViewById(R.id.genderText);
        ageText = findViewById(R.id.ageText);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Fetch and display user details
        displayUserDetails();

        // Fetch and display test history
        displayTestHistory();

        // Display task progress
        displayTaskProgress();
    }

    private void displayUserDetails() {
        // Fetch user details from the database
        String name = databaseHelper.getUserName();
        String gender = databaseHelper.getUserGender();
        String birthDate = databaseHelper.getUserBirthDate();

        // Calculate age from birth date
        int age = calculateAge(birthDate);

        // Display the details
        nameText.setText(name);
        genderText.setText("Gender: " + gender);
        ageText.setText("Age: " + age);
    }

    private int calculateAge(String birthDate) {
        if (birthDate == null || birthDate.isEmpty()) {
            return 0;
        }

        String[] parts = birthDate.split("/");
        if (parts.length < 3) {
            return 0;
        }

        int birthDay = Integer.parseInt(parts[0]);
        int birthMonth = Integer.parseInt(parts[1]);
        int birthYear = Integer.parseInt(parts[2]);

        return getAge(birthYear, birthMonth, birthDay);
    }

    private static int getAge(int birthYear, int birthMonth, int birthDay) {
        java.util.Calendar today = java.util.Calendar.getInstance();
        int currentYear = today.get(java.util.Calendar.YEAR);
        int currentMonth = today.get(java.util.Calendar.MONTH) + 1;
        int currentDay = today.get(java.util.Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--;
        }
        return age;
    }

    private void displayTestHistory() {
        // Fetch test history for each test type
        displayTestHistoryForType("phq9", findViewById(R.id.depressionCard));
        displayTestHistoryForType("gad7", findViewById(R.id.anxietyCard));
        displayTestHistoryForType("rse", findViewById(R.id.selfEsteemCard));
        displayTestHistoryForType("pss", findViewById(R.id.stressCard));
    }

    private void displayTestHistoryForType(String testName, View cardView) {
        // Implementation for displaying test history
    }

    private void displayTaskProgress() {
        // Get task counts from database
        int totalTasks = databaseHelper.getTotalAssignedTasksCount();
        int completedTasks = databaseHelper.getCompletedTasksCount();

        // Inflate and add the task progress view
        View taskProgressView = LayoutInflater.from(this).inflate(R.layout.task_progress_view,
                (ViewGroup) findViewById(android.R.id.content), false);

        TextView progressText = taskProgressView.findViewById(R.id.progressText);
        CircularProgressIndicator progressBar = taskProgressView.findViewById(R.id.progressBar);

        // Set progress text and bar
        int progress = 0;
        if (totalTasks > 0) {
            progress = (int) (((float) completedTasks / totalTasks) * 100);
        }
        progressText.setText(String.format("Task Progress: %d%%", progress));
        progressBar.setProgress(progress);

        // Add the view to the main layout
        LinearLayout mainLayout = findViewById(R.id.mainLayout); // Ensure your root layout has this id
        mainLayout.addView(taskProgressView);
    }
}