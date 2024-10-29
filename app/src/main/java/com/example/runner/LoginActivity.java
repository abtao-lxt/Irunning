package com.example.runner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // 定义用户名和密码输入框
    private EditText usernameEditText, passwordEditText;
    // 定义数据库助手类的实例，用于用户数据验证
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 设置当前Activity的布局文件

        // 初始化数据库助手类，用于后续操作数据库
        dbHelper = new UserDatabaseHelper(this);

        // 绑定用户名和密码的输入框到界面布局
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);

        // 绑定登录和注册按钮
        Button loginButton = findViewById(R.id.buttonLogin);
        Button registerButton = findViewById(R.id.buttonRegister);

        // 登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的用户名和密码，并去除两端空格
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // 验证用户名和密码是否正确
                if (validateUser(username, password)) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                    // 验证成功后，跳转到主界面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // 验证失败，显示错误提示
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 注册按钮点击事件
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // 验证用户输入的用户名和密码是否在数据库中
    private boolean validateUser(String username, String password) {
        // 获取数据库的可读实例
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 执行SQL查询，查找与输入的用户名和密码匹配的记录
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND password = ?",
                new String[]{username, password});

        // 检查是否存在匹配的记录（即cursor的数量是否大于0）
        boolean isValid = cursor.getCount() > 0;

        // 关闭游标和数据库以释放资源
        cursor.close();
        db.close();

        return isValid; // 返回验证结果
    }
}
