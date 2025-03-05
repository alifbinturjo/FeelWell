package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the feeling data from the Intent
        Intent intent = getIntent();
        String feeling = intent.getStringExtra("FEELING");

        // Display the feeling in a TextView
        TextView feelingTextView = findViewById(R.id.feelingTextView);
        if (feeling != null) {
            feelingTextView.setText("You're feeling: " + feeling);
        }

        // Button: Take an Assessment
        Button takeAssessmentButton = findViewById(R.id.takeAssessmentButton);
        takeAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DepressionActivity
                Intent assessmentIntent = new Intent(MainActivity.this, DepressionActivity.class);
                startActivity(assessmentIntent);
            }
        });

        // Button: Do Task
        Button doTaskButton = findViewById(R.id.doTaskButton);
        doTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to TaskActivity
                Intent taskIntent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(taskIntent);
            }
        });

        // ImageButton: Profile Button
        ImageButton profileButton = findViewById(R.id.imageButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
    }
}