package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView taskListView;
    private List<Map<String, String>> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        dbHelper = new DatabaseHelper(this);
        taskListView = findViewById(R.id.taskListView);

        loadTasks();
    }

    private void loadTasks() {
        taskList = new ArrayList<>();
        List<String> incompleteTasks = dbHelper.getAssignedTasks();

        for (String taskName : incompleteTasks) {
            Map<String, String> task = new HashMap<>();
            task.put("name", taskName);
            task.put("type", dbHelper.getTaskType(taskName));
            taskList.add(task);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, taskList, android.R.layout.simple_list_item_2,
                new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text2 = view.findViewById(android.R.id.text2);

                // Hide the type text unless it's "Suggestion"
                if (!taskList.get(position).get("type").equals("Suggestion")) {
                    text2.setVisibility(View.GONE);
                } else {
                    text2.setVisibility(View.VISIBLE);
                }
                return view;
            }
        };

        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, String> selectedTask = taskList.get(position);
            String taskName = selectedTask.get("name");
            String taskType = selectedTask.get("type");

            switch (taskType) {
                case "Text":
                    startTextActivity(taskName);
                    break;
                case "Time":
                    startTimeActivity(taskName);
                    break;
                case "Tick":
                    startTickActivity(taskName);
                    break;
                case "Suggestion":
                    showSuggestion(taskName);
                    break;
            }
        });
    }

    private void startTextActivity(String taskName) {
        Intent intent = new Intent(this, TextActivity.class);
        intent.putExtra("task_description", taskName);
        startActivityForResult(intent, 1);
    }

    private void startTimeActivity(String taskName) {
        Intent intent = new Intent(this, TimeActivity.class);
        intent.putExtra("task_description", taskName);
        startActivityForResult(intent, 1);
    }

    private void startTickActivity(String taskName) {
        Intent intent = new Intent(this, TickActivity.class);
        intent.putExtra("task_description", taskName);
        startActivityForResult(intent, 1);
    }

    private void showSuggestion(String taskName) {
        // Implement logic to show suggestions
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean isCompleted = data.getBooleanExtra("task_completed", false);
            String taskName = data.getStringExtra("task_name");

            if (isCompleted) {
                dbHelper.updateTaskStatus(taskName, "complete");
            }
        }
    }
}