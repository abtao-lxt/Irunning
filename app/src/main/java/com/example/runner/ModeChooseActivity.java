package com.example.runner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ModeChooseActivity extends AppCompatActivity {

    // 定义一个变量来存储当前选择的模式，初始值为1
    private int mode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_choose); // 设置当前Activity的布局文件

        // 绑定布局文件中的组件
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) 
        Button time = (Button) findViewById(R.id.time); // “时间模式”按钮
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) 
        Button dista = (Button) findViewById(R.id.dista); // “距离模式”按钮
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) 
        Button free = (Button) findViewById(R.id.free); // “自由模式”按钮
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) 
        Button go = (Button) findViewById(R.id.go); // “开始”按钮
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) 
        EditText set = (EditText) findViewById(R.id.set); // 设置目标时间或距离的输入框
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) 
        TextView danwei = (TextView) findViewById(R.id.danwei); // 显示单位的文本视图

        // 设置“时间模式”按钮的点击事件
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 1; // 设置模式为1，表示时间模式
                set.setText("30"); // 设置默认时间为30分钟
                danwei.setText("MIN"); // 设置单位为“分钟”
            }
        });

        // 设置“距离模式”按钮的点击事件
        dista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 2; // 设置模式为2，表示距离模式
                set.setText("5"); // 设置默认距离为5公里
                danwei.setText("KM"); // 设置单位为“公里”
            }
        });

        // 设置“自由模式”按钮的点击事件
        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 3; // 设置模式为3，表示自由模式
                set.setText("FR"); // 显示“FR”，代表自由模式
                danwei.setText("EE"); // 设置单位为“EE”，显示“FREE”
            }
        });

        // 设置“开始”按钮的点击事件
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个意图 (Intent) 来启动另一个Activity (RunningActivity)
                Intent intent = new Intent(ModeChooseActivity.this, RunningActivity.class);
                intent.putExtra("mode", mode); // 传递模式参数给下一个Activity
                intent.putExtra("data", set.getText()); // 传递用户设置的值给下一个Activity
                startActivity(intent); // 启动RunningActivity
            }
        });
    }
}
