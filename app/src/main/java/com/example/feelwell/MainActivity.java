package com.example.feelwell;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db != null) {
            Toast.makeText(this, "Database created successfully!", Toast.LENGTH_SHORT).show();
        }

        if (dbHelper.isUserExists()) {
            String userName = dbHelper.getUserName();
            Toast.makeText(this, "Welcome, " + userName + "!", Toast.LENGTH_LONG).show();
        } else {
            showUserInputDialog();
        }
    }

    private void showUserInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your details");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_user_input, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextBirthDate = dialogView.findViewById(R.id.editTextBirthDate);
        EditText editTextGender = dialogView.findViewById(R.id.editTextGender);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = editTextName.getText().toString().trim();
            String birthDate = editTextBirthDate.getText().toString().trim();
            String gender = editTextGender.getText().toString().trim();

            if (!name.isEmpty() && !birthDate.isEmpty() && !gender.isEmpty()) {
                dbHelper.insertUser(name, birthDate, gender);
                Toast.makeText(MainActivity.this, "Welcome, " + name + "!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}
