package com.example.feelwell;

import android.content.ContentValues;
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

public class TaskActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout tasksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        dbHelper = new DatabaseHelper(this);
        tasksContainer = findViewById(R.id.tasksContainer);

        loadIncompleteTasks();
    }

    private void loadIncompleteTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query incomplete tasks from task history
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TASK_HISTORY,
                new String[]{
                        DatabaseHelper.COLUMN_TASK_HISTORY_TASK_NAME,
                        DatabaseHelper.COLUMN_TASK_TEST_NAME,
                        DatabaseHelper.COLUMN_TASK_LEVEL,
                        DatabaseHelper.COLUMN_TASK_TYPE
                },
                DatabaseHelper.COLUMN_TASK_HISTORY_STATUS + " = ?",
                new String[]{"incomplete"},
                null, null, null
        );

        tasksContainer.removeAllViews(); // Clear existing views

        if (cursor.moveToFirst()) {
            do {
                String taskName = cursor.getString(0);
                String testName = cursor.getString(1);
                String taskLevel = cursor.getString(2);
                String taskType = cursor.getString(3);

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

                // Create inner layout for card content
                LinearLayout cardContent = new LinearLayout(this);
                cardContent.setOrientation(LinearLayout.VERTICAL);
                cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Add task name
                TextView taskTextView = new TextView(this);
                taskTextView.setText(taskName);
                taskTextView.setTextSize(18);
                cardContent.addView(taskTextView);

                // Add level indicator
                TextView levelTextView = new TextView(this);
                levelTextView.setText("Level: " + taskLevel);
                levelTextView.setTextSize(14);
                levelTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                cardContent.addView(levelTextView);

                // Add action based on task type
                if (taskType.equals("Time") || taskType.equals("Text")) {
                    Button actionButton = new Button(this);
                    actionButton.setText(taskType.equals("Time") ? "Start Timer" : "Write Response");
                    actionButton.setAllCaps(false);
                    actionButton.setOnClickListener(v -> launchTaskActivity(taskName, taskType));
                    cardContent.addView(actionButton);
                } else if (taskType.equals("Tick")) {
                    Button markButton = new Button(this);
                    markButton.setText("Mark as Completed");
                    markButton.setAllCaps(false);
                    markButton.setOnClickListener(v -> markTaskAsCompleted(taskName, testName, taskLevel));
                    cardContent.addView(markButton);
                } else if (taskType.equals("Suggestion")) {
                    TextView suggestionTag = new TextView(this);
                    suggestionTag.setText("Suggestion");
                    suggestionTag.setTextColor(getResources().getColor(R.color.colorPrimary));
                    cardContent.addView(suggestionTag);
                }

                cardView.addView(cardContent);
                tasksContainer.addView(cardView);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    private void launchTaskActivity(String taskName, String taskType) {
        if (taskType.equals("Time")) {
            Intent intent = new Intent(this, TimeActivity.class);
            intent.putExtra("task_description", taskName);
            startActivityForResult(intent, 1);
        } else if (taskType.equals("Text")) {
            Intent intent = new Intent(this, TextActivity.class);
            intent.putExtra("task_description", taskName);
            startActivityForResult(intent, 2);
        }
    }

    private void markTaskAsCompleted(String taskName, String testName, String level) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK_HISTORY_STATUS, "completed");

        db.update(
                DatabaseHelper.TABLE_TASK_HISTORY,
                values,
                DatabaseHelper.COLUMN_TASK_HISTORY_TASK_NAME + " = ? AND " +
                        DatabaseHelper.COLUMN_TASK_TEST_NAME + " = ? AND " +
                        DatabaseHelper.COLUMN_TASK_LEVEL + " = ?",
                new String[]{taskName, testName, level}
        );
        db.close();

        // Refresh the task list
        loadIncompleteTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            boolean isCompleted = data.getBooleanExtra("task_completed", false);
            String taskName = data.getStringExtra("task_name");

            if (isCompleted) {
                // Find the task in database and update its status
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_TASK_HISTORY_STATUS, "completed");

                db.update(
                        DatabaseHelper.TABLE_TASK_HISTORY,
                        values,
                        DatabaseHelper.COLUMN_TASK_HISTORY_TASK_NAME + " = ?",
                        new String[]{taskName}
                );
                db.close();

                // Refresh the task list
                loadIncompleteTasks();
            }
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}