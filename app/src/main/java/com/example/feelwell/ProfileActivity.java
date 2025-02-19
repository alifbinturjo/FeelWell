package com.example.feelwell;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        // Retrieve and display user data
        displayUserData();
    }

    private void displayUserData() {
        // Check if the user exists in the database
        if (databaseHelper.isUserExists()) {
            // Get the user's name
            String name = databaseHelper.getUserName();
            tvUserName.setText("Name: " + name);

            // Get the user's birth date and calculate age
            String birthDate = databaseHelper.getUserBirthDate();
            if (birthDate != null && !birthDate.isEmpty()) {
                int age = calculateAge(birthDate);
                tvUserAge.setText("Age: " + age);
            }

            // Get the user's gender
            String gender = databaseHelper.getUserGender();
            tvUserGender.setText("Gender: " + gender);
        } else {
            // Handle case where user data does not exist
            tvUserName.setText("Name: Not Available");
            tvUserAge.setText("Age: Not Available");
            tvUserGender.setText("Gender: Not Available");
        }
    }

    private int calculateAge(String birthDate) {
        try {
            // Define the date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dob = sdf.parse(birthDate);

            if (dob == null) return 0;

            // Get current date
            Calendar today = Calendar.getInstance();
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTime(dob);

            int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);

            // Adjust age if birth date hasn't occurred yet this year
            if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}