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
    private static final int DATABASE_VERSION = 2; // Incremented version to trigger onUpgrade


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


    public static final String COLUMN_TASK_NAME = "name";
    public static final String COLUMN_TASK_TEST_NAME = "test_name";
    public static final String COLUMN_TASK_LEVEL = "level";
    public static final String COLUMN_TASK_TYPE = "type";


    public static final String COLUMN_TASK_HISTORY_TASK_NAME = "task";
    public static final String COLUMN_TASK_HISTORY_TYPE = "type"; // Added type column
    public static final String COLUMN_TASK_HISTORY_STATUS = "status";


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
            + COLUMN_TASK_NAME + " TEXT, "
            + COLUMN_TASK_TEST_NAME + " TEXT, "
            + COLUMN_TASK_LEVEL + " TEXT, "
            + COLUMN_TASK_TYPE + " TEXT, "
            + "PRIMARY KEY (" + COLUMN_TASK_NAME + ", " + COLUMN_TASK_TEST_NAME + ", " + COLUMN_TASK_LEVEL + "), "
            + "FOREIGN KEY (" + COLUMN_TASK_TEST_NAME + ") REFERENCES " + TABLE_TESTS + "(" + COLUMN_TEST_NAME + ") ON DELETE CASCADE);";


    private static final String CREATE_TASK_HISTORY_TABLE = "CREATE TABLE " + TABLE_TASK_HISTORY + "("
            + COLUMN_TASK_HISTORY_TASK_NAME + " TEXT, "
            + COLUMN_TASK_TEST_NAME + " TEXT, "
            + COLUMN_TASK_LEVEL + " TEXT, "
            + COLUMN_TASK_HISTORY_TYPE + " TEXT, " // Added type column
            + COLUMN_TASK_HISTORY_STATUS + " TEXT, "
            + "PRIMARY KEY (" + COLUMN_TASK_HISTORY_TASK_NAME + ", " + COLUMN_TASK_TEST_NAME + ", " + COLUMN_TASK_LEVEL + "), "
            + "FOREIGN KEY (" + COLUMN_TASK_HISTORY_TASK_NAME + ", " + COLUMN_TASK_TEST_NAME + ", " + COLUMN_TASK_LEVEL + ") "
            + "REFERENCES " + TABLE_TASKS + "(" + COLUMN_TASK_NAME + ", " + COLUMN_TASK_TEST_NAME + ", " + COLUMN_TASK_LEVEL + ") ON DELETE CASCADE);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


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


        // Populate tasks table
        insertTasks(db);
    }


    private void insertTasks(SQLiteDatabase db) {
        // PHQ9 tasks
        insertTask(db, "phq9", "Minimal or No Depression", "Maintain a gratitude journal", "Text");
        insertTask(db, "phq9", "Minimal or No Depression", "Do 10 minutes of physical activity", "Time");
        insertTask(db, "phq9", "Minimal or No Depression", "Listen to uplifting music & Spend time in nature or sunlight", "Suggestion");


        insertTask(db, "phq9", "Mild Depression", "Call or text a friend", "Tick");
        insertTask(db, "phq9", "Mild Depression", "Try guided meditation for 5 minutes", "Time");
        insertTask(db, "phq9", "Mild Depression", "Reduce screen time by 30 minutes & Engage in a creative hobby (drawing, writing, music)", "Suggestion");


        insertTask(db, "phq9", "Moderate Depression", "Set one small, achievable goal today", "Text");
        insertTask(db, "phq9", "Moderate Depression", "Follow a structured morning routine", "Suggestion");
        insertTask(db, "phq9", "Moderate Depression", "Take a short walk outside", "Tick");
        insertTask(db, "phq9", "Moderate Depression", "Write down three things that went well today", "Text");


        insertTask(db, "phq9", "Moderately Severe Depression", "Challenge one negative thought and replace it with a positive one", "Text");
        insertTask(db, "phq9", "Moderately Severe Depression", "Practice deep breathing for 5 minutes", "Time");
        insertTask(db, "phq9", "Moderately Severe Depression", "Schedule a self-care activity (bath, relaxation, journaling)  & Spend time doing a light physical activity", "Suggestion");


        insertTask(db, "phq9", "Severe Depression", "Reach out to a supportive friend or family member", "Tick");
        insertTask(db, "phq9", "Severe Depression", "Follow a basic routine (wake up, shower, eat) & Avoid isolating—spend time with someone trusted & Seek professional help if feeling overwhelmed", "Suggestion");


        // GAD7 tasks
        insertTask(db, "gad7", "Minimal or No Anxiety", "Continue mindfulness practices & Maintain a balanced sleep schedule & Stay hydrated and eat nutritious meals", "Suggestion");
        insertTask(db, "gad7", "Minimal or No Anxiety", "Spend 10 minutes in a relaxing activity", "Time");


        insertTask(db, "gad7", "Mild Anxiety", "Try the 4-7-8 breathing technique", "Tick");
        insertTask(db, "gad7", "Mild Anxiety", "Identify one stressor and write down a coping strategy", "Text");
        insertTask(db, "gad7", "Mild Anxiety", "Limit caffeine and sugar intake for a day", "Tick");
        insertTask(db, "gad7", "Mild Anxiety", "Engage in stretching or light exercise", "Suggestion");


        insertTask(db, "gad7", "Moderate Anxiety", "Do the 5-4-3-2-1 grounding exercise", "Text");
        insertTask(db, "gad7", "Moderate Anxiety", "Take a 15-minute break from work or responsibilities", "Time");
        insertTask(db, "gad7", "Moderate Anxiety", "Schedule \"worry time\" to avoid excessive overthinking & Practice progressive muscle relaxation", "Suggestion");


        insertTask(db, "gad7", "Severe Anxiety", "Write down worries and challenge negative thoughts", "Text");
        insertTask(db, "gad7", "Severe Anxiety", "Seek social support—talk to a trusted person", "Tick");
        insertTask(db, "gad7", "Severe Anxiety", "Try guided visualization for relaxation & Consider professional counseling for persistent anxiety", "Suggestion");


        // PSS tasks
        insertTask(db, "pss", "Low Stress", "Maintain a consistent work-life balance & Continue engaging in relaxing activities", "Suggestion");
        insertTask(db, "pss", "Low Stress", "Practice deep breathing for 3 minutes", "Time");
        insertTask(db, "pss", "Low Stress", "Drink a glass of water and take a short break", "Tick");


        insertTask(db, "pss", "Moderate Stress", "Take a mindful walk for 10 minutes", "Time");
        insertTask(db, "pss", "Moderate Stress", "Break a large task into small steps and complete one", "Tick");
        insertTask(db, "pss", "Moderate Stress", "Listen to calming music or nature sounds & Engage in a self-care activity like journaling or meditation", "Suggestion");


        insertTask(db, "pss", "High Stress", "Reduce social media/news consumption for a day", "Tick");
        insertTask(db, "pss", "High Stress", "Practice saying 'no' to overwhelming commitments & Avoid multitasking—focus on one thing at a time & Seek professional guidance if stress feels unmanageable", "Suggestion");


        // RSE tasks
        insertTask(db, "rse", "Low Self-Esteem", "Write down one thing you appreciate about yourself", "Text");
        insertTask(db, "rse", "Low Self-Esteem", "Avoid negative self-talk for an hour", "Tick");
        insertTask(db, "rse", "Low Self-Esteem", "Do one thing outside your comfort zone today", "Tick");
        insertTask(db, "rse", "Low Self-Esteem", "List past achievements, no matter how small", "Text");


        insertTask(db, "rse", "Normal Self-Esteem", "Complete one productive task that makes you feel accomplished", "Tick");
        insertTask(db, "rse", "Normal Self-Esteem", "Engage in an activity that boosts your confidence & Spend time with people who uplift you", "Suggestion");
        insertTask(db, "rse", "Normal Self-Esteem", "Maintain good posture and body language for 5 minutes", "Time");


        insertTask(db, "rse", "High Self-Esteem", "Encourage or compliment someone today", "Tick");
        insertTask(db, "rse", "High Self-Esteem", "Share a success story or experience with others", "Tick");
        insertTask(db, "rse", "High Self-Esteem", "Take on a new challenge or leadership role & Reflect on what has helped build your confidence", "Suggestion");
    }


    private void insertTask(SQLiteDatabase db, String testName, String level, String name, String type) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TEST_NAME, testName);
        values.put(COLUMN_TASK_LEVEL, level);
        values.put(COLUMN_TASK_NAME, name);
        values.put(COLUMN_TASK_TYPE, type);
        db.insert(TABLE_TASKS, null, values);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add the new column to the TASK_HISTORY table
            db.execSQL("ALTER TABLE " + TABLE_TASK_HISTORY + " ADD COLUMN " + COLUMN_TASK_HISTORY_TYPE + " TEXT");
        }
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


    public void deleteTasksForTest(String testName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK_HISTORY,
                COLUMN_TASK_HISTORY_TASK_NAME + " IN (SELECT " + COLUMN_TASK_NAME +
                        " FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_TEST_NAME + " = ?)",
                new String[]{testName});


        db.delete(TABLE_TASK_HISTORY, COLUMN_TASK_TEST_NAME + " = ?", new String[]{testName});
        db.close();
    }


    public void assignTasksForTest(String testName, String level) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.query(TABLE_TASKS,
                new String[]{COLUMN_TASK_NAME, COLUMN_TASK_TEST_NAME, COLUMN_TASK_LEVEL, COLUMN_TASK_TYPE},
                COLUMN_TASK_TEST_NAME + " = ? AND " + COLUMN_TASK_LEVEL + " = ?",
                new String[]{testName, level},
                null, null, null);


        if (cursor.moveToFirst()) {
            do {
                String taskName = cursor.getString(0);
                String taskTestName = cursor.getString(1);
                String taskLevel = cursor.getString(2);
                String taskType = cursor.getString(3);


                ContentValues values = new ContentValues();
                values.put(COLUMN_TASK_HISTORY_TASK_NAME, taskName);
                values.put(COLUMN_TASK_TEST_NAME, taskTestName);
                values.put(COLUMN_TASK_LEVEL, taskLevel);
                values.put(COLUMN_TASK_HISTORY_TYPE, taskType); // Add type to values
                values.put(COLUMN_TASK_HISTORY_STATUS, "incomplete");


                db.insertWithOnConflict(TABLE_TASK_HISTORY,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
    }


    public List<String> getAssignedTasks() {
        List<String> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_TASK_HISTORY,
                new String[]{COLUMN_TASK_HISTORY_TASK_NAME},
                COLUMN_TASK_HISTORY_STATUS + " = ?",
                new String[]{"incomplete"},
                null, null, null);


        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }


        cursor.close();
        return tasks;
    }


    public String getTaskType(String taskName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS,
                new String[]{COLUMN_TASK_TYPE},
                COLUMN_TASK_NAME + " = ?",
                new String[]{taskName},
                null, null, null);

        String taskType = ""; // Default to an empty string

        if (cursor.moveToFirst()) {
            taskType = cursor.getString(0); // Get the task type from the first column
        }
        cursor.close();

        // Only return the type if it is "suggestion"
        return taskType;
    }



    public void updateTaskStatus(String taskName, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_HISTORY_STATUS, status);
        db.update(TABLE_TASK_HISTORY, values, COLUMN_TASK_HISTORY_TASK_NAME + " = ?", new String[]{taskName});
        db.close();
    }

    // Add these methods to DatabaseHelper.java
    public int getTotalAssignedTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_TASK_HISTORY +
                        " WHERE " + COLUMN_TASK_HISTORY_TYPE + " != 'Suggestion'",
                null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getCompletedTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_TASK_HISTORY +
                        " WHERE " + COLUMN_TASK_HISTORY_STATUS + " = 'complete' AND " +
                        COLUMN_TASK_HISTORY_TYPE + " != 'Suggestion'",
                null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


}



