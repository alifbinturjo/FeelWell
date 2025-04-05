package com.example.feelwell;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DefaultTaskActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String feeling;
    private LinearLayout tasksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_task);

        dbHelper = new DatabaseHelper(this);
        tasksContainer = findViewById(R.id.tasksContainer);

        // Get the feeling from intent
        feeling = getIntent().getStringExtra("feeling");
        if (feeling == null) {
            feeling = "depression"; // default
        }

        // Convert feeling to test name
        String testName = convertFeelingToTestName(feeling);

        // Load tasks for this test
        loadTasks(testName);
    }

    private String convertFeelingToTestName(String feeling) {
        switch (feeling.toLowerCase()) {
            case "depression":
                return "phq9";
            case "anxiety":
                return "gad7";
            case "stress":
                return "pss";
            case "selfesteem":
                return "rse";
            default:
                return "phq9"; // default
        }
    }

    private void loadTasks(String testName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query tasks for the specific test
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TASKS,
                new String[]{
                        DatabaseHelper.COLUMN_TASK_NAME,
                        DatabaseHelper.COLUMN_TASK_TYPE,
                        DatabaseHelper.COLUMN_TASK_LEVEL
                },
                DatabaseHelper.COLUMN_TASK_TEST_NAME + " = ?",
                new String[]{testName},
                null, null, null
        );

        tasksContainer.removeAllViews(); // Clear existing views

        if (cursor.moveToFirst()) {
            do {
                String taskName = cursor.getString(0);
                String taskType = cursor.getString(1);
                String taskLevel = cursor.getString(2);

                // Create card for each task
                CardView cardView = new CardView(this);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(16, 8, 16, 8);
                cardView.setLayoutParams(cardParams);
                cardView.setCardElevation(8);
                cardView.setRadius(12);
                cardView.setContentPadding(16, 16, 16, 16);

                // Create button for task
                Button taskButton = new Button(this);
                taskButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                taskButton.setText(taskName);
                taskButton.setAllCaps(false);
                taskButton.setTextSize(16);
                taskButton.setBackgroundResource(R.drawable.card_background); // Add your own drawable
                taskButton.setPadding(16, 16, 16, 16);

                // Set click listener based on task type
                taskButton.setOnClickListener(v -> {
                    if (taskType.equals("Time")) {
                        Intent intent = new Intent(DefaultTaskActivity.this, TimeActivity.class);
                        intent.putExtra("task_description", taskName);
                        startActivityForResult(intent, 1);
                    } else if (taskType.equals("Text")) {
                        Intent intent = new Intent(DefaultTaskActivity.this, TextActivity.class);
                        intent.putExtra("task_description", taskName);
                        startActivityForResult(intent, 2);
                    }
                    // For "Tick" and "Suggestion" types, we don't redirect to other activities
                });

                cardView.addView(taskButton);
                tasksContainer.addView(cardView);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            boolean isCompleted = data.getBooleanExtra("task_completed", false);
            String taskName = data.getStringExtra("task_name");

            if (isCompleted) {
                // Find the button with this task name and mark it as completed
                markTaskAsCompleted(taskName);
            }
        }
    }

    private void markTaskAsCompleted(String taskName) {
        for (int i = 0; i < tasksContainer.getChildCount(); i++) {
            View child = tasksContainer.getChildAt(i);
            if (child instanceof CardView) {
                CardView cardView = (CardView) child;
                View buttonView = cardView.getChildAt(0);
                if (buttonView instanceof Button) {
                    Button button = (Button) buttonView;
                    if (button.getText().toString().equals(taskName)) {
                        // Strike through text
                        button.setPaintFlags(button.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        // Make it unpressable
                        button.setClickable(false);
                        button.setAlpha(0.6f);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}