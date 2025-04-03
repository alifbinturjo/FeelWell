package com.example.feelwell;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DefaultTaskActivity extends AppCompatActivity {

    private static final String TAG = "DefaultTaskActivity";
    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private DatabaseHelper dbHelper;
    private String currentFeeling;

    // Activity result launcher
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        boolean taskCompleted = result.getData().getBooleanExtra("task_completed", false);
                        String taskName = result.getData().getStringExtra("task_name");


                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_task);

        currentFeeling = getIntent().getStringExtra("FEELING");
        if (currentFeeling == null) {
            currentFeeling = "depression";
        }

        currentFeeling = currentFeeling.toLowerCase().trim();
        setTitle("Tasks for " + capitalizeFirstLetter(currentFeeling));

        dbHelper = new DatabaseHelper(this);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void loadTasks() {
        List<Task> tasks = getFilteredTasks();
        taskAdapter = new TaskAdapter(tasks);
        tasksRecyclerView.setAdapter(taskAdapter);
        Log.d(TAG, "Loaded " + tasks.size() + " tasks for feeling: " + currentFeeling);
    }

    private List<Task> getFilteredTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String testFilter = getTestFilterForFeeling(currentFeeling);

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TASKS +
                " WHERE " + DatabaseHelper.COLUMN_TASK_TYPE + " IN ('Text', 'Time')" +
                " AND " + DatabaseHelper.COLUMN_TASK_TEST_NAME + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{testFilter})) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_NAME);
                int typeIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TYPE);
                int testNameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TEST_NAME);
                int levelIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_LEVEL);

                do {
                    String name = cursor.getString(nameIndex);
                    String type = cursor.getString(typeIndex);
                    String testName = cursor.getString(testNameIndex);
                    String level = cursor.getString(levelIndex);



                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error querying tasks", e);
        }

        return tasks;
    }

    private String getTestFilterForFeeling(String feeling) {
        if (feeling.contains("stress")) {
            return "pss";
        } else if (feeling.contains("self") || feeling.contains("esteem")) {
            return "rse";
        } else if (feeling.contains("anxiety")) {
            return "gad7";
        } else if (feeling.contains("depress")) {
            return "phq9";
        }
        return "phq9";
    }





    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            Task task = tasks.get(position);

            holder.taskNameTextView.setText(task.name);
            if (task.isCompleted) {
                // Completed task styling
                holder.taskNameTextView.setPaintFlags(holder.taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.taskNameTextView.setAlpha(0.5f); // Make text appear faded
                holder.itemView.setClickable(false); // Disable clicking on completed tasks
                holder.itemView.setEnabled(false);
            } else {
                // Incomplete task styling
                holder.taskNameTextView.setPaintFlags(holder.taskNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.taskNameTextView.setAlpha(1f); // Full opacity
                holder.itemView.setClickable(true); // Enable clicking on incomplete tasks
                holder.itemView.setEnabled(true);
            }

            // Hide type and status TextViews as requested
            holder.taskTypeTextView.setVisibility(View.GONE);
            holder.taskStatusTextView.setVisibility(View.GONE);

            // Set click listener for the entire card
            holder.itemView.setOnClickListener(v -> {
                if (!task.isCompleted) {
                    Intent intent;
                    if ("Time".equalsIgnoreCase(task.type)) {
                        intent = new Intent(DefaultTaskActivity.this, TimeActivity.class);
                    } else {
                        intent = new Intent(DefaultTaskActivity.this, TextActivity.class);
                    }
                    intent.putExtra("task_description", task.name);
                    activityResultLauncher.launch(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView taskNameTextView;
            TextView taskTypeTextView;
            TextView taskStatusTextView;

            public TaskViewHolder(View itemView) {
                super(itemView);
                taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
                taskTypeTextView = itemView.findViewById(R.id.taskTypeTextView);
                taskStatusTextView = itemView.findViewById(R.id.taskStatusTextView);
            }
        }
    }

    private static class Task {
        String name;
        String type;
        String testName;
        String level;
        boolean isCompleted;

        public Task(String name, String type, String testName, String level, boolean isCompleted) {
            this.name = name;
            this.type = type;
            this.testName = testName;
            this.level = level;
            this.isCompleted = isCompleted;
        }
    }
}