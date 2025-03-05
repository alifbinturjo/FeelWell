package com.example.feelwell;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView tvUserName, tvUserAge, tvUserGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        tvUserName = findViewById(R.id.tvUserName);
        tvUserAge = findViewById(R.id.tvUserAge);
        tvUserGender = findViewById(R.id.tvUserGender);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Display user data
        displayUserData();
    }

    private void displayUserData() {
        if (databaseHelper.isUserExists()) {
            tvUserName.setText(databaseHelper.getUserName());

            // Calculate age
            String birthDate = databaseHelper.getUserBirthDate();
            if (birthDate != null && !birthDate.isEmpty()) {
                tvUserAge.setText(String.valueOf(calculateAge(birthDate)));
            }

            // Display gender
            tvUserGender.setText(databaseHelper.getUserGender());
        }
    }

    private int calculateAge(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(birthDate, formatter);
            return Period.between(dob, LocalDate.now()).getYears();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
