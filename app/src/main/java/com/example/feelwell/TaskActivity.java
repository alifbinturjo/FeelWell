package com.example.feelwell;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ListView taskListView = findViewById(R.id.taskListView);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get all tasks from history
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT th.task, th.status FROM TASK_HISTORY th " +
                        "JOIN TASKS t ON th.task = t.name " +
                        "ORDER BY t.test_name, th.status", null);

        ArrayList<String> taskList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String taskName = cursor.getString(0);
            String status = cursor.getString(1);
            taskList.add(taskName + " - " + status);
        }
        cursor.close();

        // Display in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                taskList
        );
        taskListView.setAdapter(adapter);

        // Add click listener to mark tasks as complete
        taskListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTask = taskList.get(position).split(" - ")[0];
            dbHelper.getWritableDatabase().execSQL(
                    "UPDATE TASK_HISTORY SET status = 'Completed' WHERE task = ?",
                    new String[]{selectedTask});

            // Refresh the list
            recreate();
        });
    }
}