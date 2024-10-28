package com.example.runner;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new UserDatabaseHelper(this);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        Button registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    saveUser(username, password);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();  // 返回登录界面
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("Users", null, values);
        db.close();
    }
}
