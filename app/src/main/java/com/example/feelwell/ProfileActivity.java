package com.example.feelwell;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView tvUserName, tvUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDetails = findViewById(R.id.tvUserDetails);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Display user data
        displayUserData();
    }

    private void displayUserData() {
        if (databaseHelper.isUserExists()) {
            String name = databaseHelper.getUserName();
            tvUserName.setText(name);

            // Retrieve Gender and Birthdate
            String gender = databaseHelper.getUserGender();
            String birthDate = databaseHelper.getUserBirthDate();
            String ageText = "";

            if (birthDate != null && !birthDate.isEmpty()) {
                int age = calculateAge(birthDate);
                ageText = "Age: " + (age > 0 ? age : "N/A");  // Avoid showing 0
            }

            // Set gender and age together
            tvUserDetails.setText(gender + "  |  " + ageText);
        }
    }

    private int calculateAge(String birthDate) {
        try {
            // Debug: Print birthDate to check its format
            System.out.println("Birthdate from DB: " + birthDate);

            // Ensure the date format matches what’s stored in the DB
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(birthDate, formatter);
            return Period.between(dob, LocalDate.now()).getYears();
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Return 0 if there's an error
        }
    }

}
