package com.example.runner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Records.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Running ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "time TEXT, "
            + "calorie TEXT, "
            + "date1 TEXT, "
            + "date2 TEXT, "
            + "distance TEXT)";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Running");
        onCreate(db);
    }
}