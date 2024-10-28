package com.example.runner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView totalDistanceTextView;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.username_text);
        totalDistanceTextView = findViewById(R.id.total_distance_text);

        dbHelper = new UserDatabaseHelper(this);

        // 从Intent中获取传递的用户名
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            displayUserInfo(username);
        } else {
            usernameTextView.setText("无法获取用户信息");
            totalDistanceTextView.setText("暂无记录");
        }
    }

    private void displayUserInfo(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 显示用户名
        usernameTextView.setText("账户名称: " + username);

        // 查询用户的总运动距离
        Cursor cursor = db.rawQuery("SELECT total_distance FROM UserTotalDistance WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            try {
                double totalDistance = cursor.getDouble(cursor.getColumnIndexOrThrow("total_distance"));
                totalDistanceTextView.setText("运动总距离: " + totalDistance + " 公里");
            } catch (IllegalArgumentException e) {
                totalDistanceTextView.setText("运动总距离: 暂无记录");
            }
        } else {
            totalDistanceTextView.setText("运动总距离: 暂无记录");
        }
        cursor.close();
        db.close();
    }
}
