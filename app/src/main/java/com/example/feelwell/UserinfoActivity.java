package com.example.feelwell;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class UserinfoActivity extends AppCompatActivity {

    private EditText editTextName;
    private TextView textViewDOB;
    private Spinner spinnerGender;
    private Button buttonPickDate, buttonSubmit;
    private String selectedGender = "";
    private String selectedDate = "";
    private DatabaseHelper databaseHelper;  // Database Helper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        editTextName = findViewById(R.id.editTextName);
        textViewDOB = findViewById(R.id.textViewDOB);
        spinnerGender = findViewById(R.id.spinnerGender);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        databaseHelper = new DatabaseHelper(this);  // Initialize DatabaseHelper

        setupGenderSpinner();
        setupDatePicker();
        setupSubmitButton();
    }

    private void setupGenderSpinner() {
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        spinnerGender.setAdapter(adapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = genders[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGender = "";
            }
        });
    }

    private void setupDatePicker() {
        buttonPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    UserinfoActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        textViewDOB.setText("Date of Birth: " + selectedDate);
                    },
                    year, month, day);

            datePickerDialog.show();
        });
    }

    private void setupSubmitButton() {
        buttonSubmit.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();

            if (name.isEmpty() || selectedDate.isEmpty() || selectedGender.isEmpty()) {
                Toast.makeText(UserinfoActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Insert user into database
                databaseHelper.insertUser(name, selectedDate, selectedGender);
                Toast.makeText(UserinfoActivity.this, "User Info Saved", Toast.LENGTH_SHORT).show();

                // Redirect to FeelingPromptActivity
                Intent intent = new Intent(UserinfoActivity.this, FeelingPromptActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
    }
}
