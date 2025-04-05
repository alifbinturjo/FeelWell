package com.example.feelwell;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class TickActivity extends AppCompatActivity {


    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tick);


        description = getIntent().getStringExtra("task_description");
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);


        Button completeButton = findViewById(R.id.completeButton);
        completeButton.setOnClickListener(v -> {
            setCompletionResult(true);
            finish();
        });
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



