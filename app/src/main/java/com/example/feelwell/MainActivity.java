package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the feeling data from the Intent
        Intent intent = getIntent();
        String feeling = intent.getStringExtra("FEELING");

        // Set engaging text
        TextView engagingTextView = findViewById(R.id.engagingTextView);
        engagingTextView.setText("No worries, we've got you covered!");

        // Button: Take an Assessment
        Button takeAssessmentButton = findViewById(R.id.takeAssessmentButton);
        takeAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to different activity based on feeling
                Intent assessmentIntent;
                if ("Depression".equals(feeling)) {
                    assessmentIntent = new Intent(MainActivity.this, DepressionActivity.class);
                } else if ("Stress".equals(feeling)) {
                    assessmentIntent = new Intent(MainActivity.this, StressActivity.class);
                } else if ("Anxiety".equals(feeling)) {
                    assessmentIntent = new Intent(MainActivity.this, AnxietyActivity.class);
                } else {
                    assessmentIntent = new Intent(MainActivity.this, LowSelfEsteemActivity.class);
                }
                startActivity(assessmentIntent);
                finish();
            }
        });

        // Button: Do a Task - Modified to pass the feeling
        Button doTaskButton = findViewById(R.id.doTaskButton);
        doTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskIntent = new Intent(MainActivity.this, DefaultTaskActivity.class);
                // Pass the feeling to DefaultTaskActivity
                taskIntent.putExtra("feeling", feeling);
                startActivity(taskIntent);
            }
        });

        // ImageButton: Profile
        ImageButton profileButton = findViewById(R.id.imageButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
    }
}