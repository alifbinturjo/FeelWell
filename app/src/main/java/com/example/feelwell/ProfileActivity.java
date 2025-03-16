package com.example.feelwell;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
        // Check if birthDate is null or empty
        if (birthDate == null || birthDate.isEmpty()) {

            return 0;
        }

        // Split the birth date into day, month, and year using "/"
        String[] parts = birthDate.split("/");
        if (parts.length < 3) {

            return 0;
        }

        // Parse day, month, and year
        int birthDay = Integer.parseInt(parts[0]);
        int birthMonth = Integer.parseInt(parts[1]);
        int birthYear = Integer.parseInt(parts[2]);

        // Log the parsed parts for debugging


        // Calculate age
        int age = getAge(birthYear, birthMonth, birthDay);

        return age;
    }

    private static int getAge(int birthYear, int birthMonth, int birthDay) {
        java.util.Calendar today = java.util.Calendar.getInstance();
        int currentYear = today.get(java.util.Calendar.YEAR);
        int currentMonth = today.get(java.util.Calendar.MONTH) + 1; // Month is 0-based
        int currentDay = today.get(java.util.Calendar.DAY_OF_MONTH);

        // Calculate age
        int age = currentYear - birthYear;
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--; // Adjust if birthday hasn't occurred yet this year
        }
        return age;
    }
}