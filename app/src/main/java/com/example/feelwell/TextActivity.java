package com.example.feelwell;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TextActivity extends AppCompatActivity {
    private EditText responseEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        // Get task description from intent
        String description = getIntent().getStringExtra("task_description");
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);

        responseEditText = findViewById(R.id.responseEditText);
        Button doneButton = findViewById(R.id.doneButton);

        ((android.view.View) doneButton).setOnClickListener(v -> {
            // Save response if needed
            String response = responseEditText.getText().toString();
            saveResponse(description, response);

            // Return to TaskActivity
            finish();
        });
    }

    private void saveResponse(String taskDescription, String response) {
        // Implement your saving logic here
        // Example: SharedPreferences or database
    }
}