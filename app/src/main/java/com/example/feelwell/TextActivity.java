package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TextActivity extends AppCompatActivity {
    private EditText responseEditText;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        description = getIntent().getStringExtra("task_description");
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);

        responseEditText = findViewById(R.id.responseEditText);
        Button doneButton = findViewById(R.id.doneButton);

        doneButton.setOnClickListener(v -> {
            String response = responseEditText.getText().toString();
            if (!response.isEmpty()) {
                saveResponse(description, response);
                setCompletionResult(true);
                finish();
            }
        });
    }

    private void saveResponse(String taskDescription, String response) {
        // Implement your saving logic here
    }

    private void setCompletionResult(boolean isCompleted) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_completed", isCompleted);
        resultIntent.putExtra("task_name", description);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void onBackPressed() {
        setCompletionResult(false);
        super.onBackPressed();
    }
}