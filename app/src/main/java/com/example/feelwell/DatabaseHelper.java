package com.example.feelwell;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "feelwell.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER = "user";

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BD = "birth_date";
    private static final String COLUMN_GENDER = "gender";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_BD + " DATE, "
            + COLUMN_GENDER + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void insertUser(String name, String birthDate, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // This was missing
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_BD, birthDate);
        values.put(COLUMN_GENDER, gender);
        db.insert(TABLE_USER, null, values);
        db.close();
    }
}
