package com.example.runner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// SQLiteOpenHelper的子类，用于管理用户数据库
public class UserDatabaseHelper extends SQLiteOpenHelper {
    // 数据库名称和版本
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 2;  // 数据库版本

    // 构造函数，用于创建数据库
    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 当数据库首次创建时调用，用于初始化表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建Users表，用于存储用户登录信息
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "  // 主键，自增
                + "username TEXT, "                        // 用户名字段
                + "password TEXT)";                        // 密码字段
        db.execSQL(CREATE_USER_TABLE);

        // 创建UserTotalDistance表，用于存储每个用户的总距离
        String CREATE_TOTAL_DISTANCE_TABLE = "CREATE TABLE IF NOT EXISTS UserTotalDistance ("
                + "username TEXT PRIMARY KEY, "           // 用户名作为主键
                + "total_distance REAL DEFAULT 0)";       // 总距离字段，默认为0
        db.execSQL(CREATE_TOTAL_DISTANCE_TABLE);
    }

    // 当数据库需要升级时调用，例如在数据库版本更改时
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 检查旧版本号，若小于2则创建UserTotalDistance表
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS UserTotalDistance (username TEXT PRIMARY KEY, total_distance REAL DEFAULT 0)");
        }
    }
}
