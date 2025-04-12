package com.example.feelwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView taskListView;
    private Map<String, List<Map<String, String>>> groupedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        dbHelper = new DatabaseHelper(this);
        taskListView = findViewById(R.id.taskListView);

        loadTasks();
    }

    private void loadTasks() {
        groupedTasks = new HashMap<>();
        List<String> incompleteTasks = dbHelper.getAssignedTasks();

        // Initialize groups
        groupedTasks.put("Time", new ArrayList<>());
        groupedTasks.put("Text", new ArrayList<>());
        groupedTasks.put("Tick", new ArrayList<>());
        groupedTasks.put("Suggestion", new ArrayList<>());

        // Group tasks by type
        for (String taskName : incompleteTasks) {
            String taskType = dbHelper.getTaskType(taskName);
            Map<String, String> task = new HashMap<>();
            task.put("name", taskName);
            task.put("type", taskType);

            if (groupedTasks.containsKey(taskType)) {
                groupedTasks.get(taskType).add(task);
            }
        }

        // Create and set custom adapter
        TaskGroupAdapter adapter = new TaskGroupAdapter();
        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener((parent, view, position, id) -> {
            // Find which item was clicked
            int section = 0;
            for (String type : groupedTasks.keySet()) {
                List<Map<String, String>> tasks = groupedTasks.get(type);
                if (tasks.isEmpty()) continue;

                if (position == section) {
                    // Header clicked, do nothing
                    return;
                } else if (position < section + tasks.size() + 1) {
                    // Task clicked
                    Map<String, String> selectedTask = tasks.get(position - section - 1);
                    String taskName = selectedTask.get("name");
                    handleTaskClick(type, taskName);
                    return;
                }
                section += tasks.size() + 1;
            }
        });
    }

    private void handleTaskClick(String taskType, String taskName) {
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

    private class TaskGroupAdapter extends BaseAdapter {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        @Override
        public int getCount() {
            int count = 0;
            for (List<Map<String, String>> tasks : groupedTasks.values()) {
                if (!tasks.isEmpty()) {
                    count += tasks.size() + 1; // +1 for header
                }
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            int section = 0;
            for (Map.Entry<String, List<Map<String, String>>> entry : groupedTasks.entrySet()) {
                List<Map<String, String>> tasks = entry.getValue();
                if (tasks.isEmpty()) continue;

                if (position == section) {
                    return entry.getKey(); // Header
                } else if (position < section + tasks.size() + 1) {
                    return tasks.get(position - section - 1); // Task
                }
                section += tasks.size() + 1;
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            int section = 0;
            for (List<Map<String, String>> tasks : groupedTasks.values()) {
                if (tasks.isEmpty()) continue;

                if (position == section) {
                    return TYPE_HEADER;
                } else if (position < section + tasks.size() + 1) {
                    return TYPE_ITEM;
                }
                section += tasks.size() + 1;
            }
            return TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 2; // We have two types: header and item
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getItemViewType(position);

            if (viewType == TYPE_HEADER) {
                // Header view (no changes needed)
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_header, parent, false);
                }

                String type = (String) getItem(position);
                TextView headerText = convertView.findViewById(R.id.headerText);

                switch (type) {
                    case "Time":
                        headerText.setText("Time Tasks");
                        break;
                    case "Text":
                        headerText.setText("Writing Tasks");
                        break;
                    case "Tick":
                        headerText.setText("Check Tasks");
                        break;
                    case "Suggestion":
                        headerText.setText("Suggestions");
                        break;
                    default:
                        headerText.setText(type + " Tasks");
                }

                return convertView;
            } else {
                // Item view - add divider
                if (convertView == null || convertView.getTag() == null) {
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_with_divider, parent, false);
                    convertView.setTag("ITEM");
                }

                @SuppressWarnings("unchecked")
                Map<String, String> task = (Map<String, String>) getItem(position);
                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setText(task.get("name"));

                return convertView;
            }
        }
    }
}
