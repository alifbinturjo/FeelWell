package com.example.feelwell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "feelwell.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER = "USER";
    public static final String TABLE_DOCTORS = "DOCTORS";
    public static final String TABLE_TESTS = "TESTS";
    public static final String TABLE_TEST_HISTORY = "TEST_HISTORY";
    public static final String TABLE_TASKS = "TASKS";
    public static final String TABLE_TASK_HISTORY = "TASK_HISTORY";

    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_BIRTH_DATE = "birth_date";
    private static final String COLUMN_USER_GENDER = "gender";

    private static final String COLUMN_DOCTOR_NAME = "name";
    private static final String COLUMN_DOCTOR_EMAIL = "email";
    private static final String COLUMN_DOCTOR_FIELD = "field";
    private static final String COLUMN_DOCTOR_ADDRESS = "address";

    private static final String COLUMN_TEST_NAME = "test_name";

    private static final String COLUMN_TEST_HISTORY_TEST_NAME = "test_name";
    private static final String COLUMN_TEST_HISTORY_DATE = "date";
    private static final String COLUMN_TEST_HISTORY_RESULT = "score";

    private static final String COLUMN_TASK_NAME = "task";
    private static final String COLUMN_TASK_TEST_NAME = "test_name";
    private static final String COLUMN_TASK_LEVEL = "level";

    private static final String COLUMN_TASK_HISTORY_TASK_NAME = "task";
    private static final String COLUMN_TASK_HISTORY_DATE = "date";
    private static final String COLUMN_TASK_HISTORY_STATUS = "status";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_NAME + " TEXT PRIMARY KEY, "
            + COLUMN_USER_BIRTH_DATE + " DATE, "
            + COLUMN_USER_GENDER + " TEXT);";

    private static final String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_DOCTORS + "("
            + COLUMN_DOCTOR_NAME + " TEXT NOT NULL, "
            + COLUMN_DOCTOR_EMAIL + " TEXT PRIMARY KEY, "
            + COLUMN_DOCTOR_FIELD + " TEXT, "
            + COLUMN_DOCTOR_ADDRESS + " TEXT);";

    private static final String CREATE_TESTS_TABLE = "CREATE TABLE " + TABLE_TESTS + "("
            + COLUMN_TEST_NAME + " TEXT PRIMARY KEY);";

    private static final String CREATE_TEST_HISTORY_TABLE = "CREATE TABLE " + TABLE_TEST_HISTORY + "("
            + COLUMN_TEST_HISTORY_TEST_NAME + " TEXT, "
            + COLUMN_TEST_HISTORY_DATE + " DATE, "
            + COLUMN_TEST_HISTORY_RESULT + " INTEGER, " // Changed to INTEGER
            + "PRIMARY KEY (" + COLUMN_TEST_HISTORY_TEST_NAME + ", " + COLUMN_TEST_HISTORY_DATE + "), "
            + "FOREIGN KEY (" + COLUMN_TEST_HISTORY_TEST_NAME + ") REFERENCES " + TABLE_TESTS + "(" + COLUMN_TEST_NAME + ") ON DELETE CASCADE);";

    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + COLUMN_TASK_NAME + " TEXT PRIMARY KEY, "
            + COLUMN_TASK_TEST_NAME + " TEXT, "
            + COLUMN_TASK_LEVEL + " TEXT, "
            + "FOREIGN KEY (" + COLUMN_TASK_TEST_NAME + ") REFERENCES " + TABLE_TESTS + "(" + COLUMN_TEST_NAME + ") ON DELETE CASCADE);";

    private static final String CREATE_TASK_HISTORY_TABLE = "CREATE TABLE " + TABLE_TASK_HISTORY + "("
            + COLUMN_TASK_HISTORY_TASK_NAME + " TEXT, "
            + COLUMN_TASK_HISTORY_DATE + " DATE, "
            + COLUMN_TASK_HISTORY_STATUS + " TEXT, "
            + "PRIMARY KEY (" + COLUMN_TASK_HISTORY_TASK_NAME + ", " + COLUMN_TASK_HISTORY_DATE + "), "
            + "FOREIGN KEY (" + COLUMN_TASK_HISTORY_TASK_NAME + ") REFERENCES " + TABLE_TASKS + "(" + COLUMN_TASK_NAME + ") ON DELETE CASCADE);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Insert predefined test names into the TESTS table


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_DOCTORS_TABLE);
        db.execSQL(CREATE_TESTS_TABLE);
        db.execSQL(CREATE_TEST_HISTORY_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_TASK_HISTORY_TABLE);
        // Insert predefined test names
        String[] testNames = {"pss", "rse", "gad7", "phq9"};
        for (String testName : testNames) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEST_NAME, testName);
            db.insert(TABLE_TESTS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_HISTORY);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;"); // Enable foreign key constraints
    }

    // Check if user exists in the database
    public boolean isUserExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_NAME + " FROM " + TABLE_USER + " LIMIT 1", null);
        boolean exists = cursor.moveToFirst(); // Use moveToFirst to check if there is a result
        cursor.close();
        return exists;
    }

    // Get the user's name
    public String getUserName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_NAME + " FROM " + TABLE_USER + " LIMIT 1", null);
        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    // Insert user into the database
    public void insertUser(String name, String birthDate, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_BIRTH_DATE, birthDate);
        values.put(COLUMN_USER_GENDER, gender);
        db.insert(TABLE_USER, null, values);
        db.close();  // Close the database connection after inserting
    }
    // Get the user's birth date
    public String getUserBirthDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_BIRTH_DATE + " FROM " + TABLE_USER + " LIMIT 1", null);
        String birthDate = "";
        if (cursor.moveToFirst()) {
            birthDate = cursor.getString(0);
        }
        cursor.close();
        return birthDate;
    }

    // Get the user's gender
    public String getUserGender() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_GENDER + " FROM " + TABLE_USER + " LIMIT 1", null);
        String gender = "";
        if (cursor.moveToFirst()) {
            gender = cursor.getString(0);
        }
        cursor.close();
        return gender;
    }

    // Insert test history into the TEST_HISTORY table
    public void insertTestHistory(String testName, String date, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEST_HISTORY_TEST_NAME, testName);
        values.put(COLUMN_TEST_HISTORY_DATE, date);
        values.put(COLUMN_TEST_HISTORY_RESULT, score); // Storing score as integer
        db.insert(TABLE_TEST_HISTORY, null, values);
        db.close();
    }

    public String getLastTestDate(String testName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(date) FROM test_history WHERE test_name = ?", new String[]{testName});

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            String lastTestDate = cursor.getString(0);
            cursor.close();
            return lastTestDate; // Returns the last test date
        }

        cursor.close();
        return null; // No previous test found
    }

    public List<TestHistory> getTestHistory(String testName, int limit) {
        List<TestHistory> testHistories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TEST_HISTORY_DATE + ", " + COLUMN_TEST_HISTORY_RESULT +
                " FROM " + TABLE_TEST_HISTORY +
                " WHERE " + COLUMN_TEST_HISTORY_TEST_NAME + " = ?" +
                " ORDER BY " + COLUMN_TEST_HISTORY_DATE + " DESC LIMIT ?", new String[]{testName, String.valueOf(limit)});

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                int score = cursor.getInt(1);
                int totalScore = getTotalScoreForTest(testName); // Implement this method based on your test scoring logic
                testHistories.add(new TestHistory(date, score, totalScore));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return testHistories;
    }

    private int getTotalScoreForTest(String testName) {
        // Implement logic to return the total possible score for the test
        switch (testName) {
            case "phq9":
                return 27; // Example total score for PHQ-9
            case "gad7":
                return 21; // Example total score for GAD-7
            case "rse":
                return 30; // Example total score for RSE
            case "pss":
                return 40; // Example total score for PSS
            default:
                return 0;
        }
    }

}
