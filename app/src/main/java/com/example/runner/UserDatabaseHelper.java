package com.example.runner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 2;  // 确保已升级版本

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT, "
                + "password TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_TOTAL_DISTANCE_TABLE = "CREATE TABLE IF NOT EXISTS UserTotalDistance ("
                + "username TEXT PRIMARY KEY, "
                + "total_distance REAL DEFAULT 0)";
        db.execSQL(CREATE_TOTAL_DISTANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS UserTotalDistance (username TEXT PRIMARY KEY, total_distance REAL DEFAULT 0)");
        }
    }
}
