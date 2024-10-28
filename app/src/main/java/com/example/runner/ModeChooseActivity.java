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

    private int mode=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_choose);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button time=(Button) findViewById(R.id.time);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button dista=(Button) findViewById(R.id.dista);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button free=(Button) findViewById(R.id.free);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button go=(Button) findViewById(R.id.go);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText set=(EditText) findViewById(R.id.set);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView danwei=(TextView) findViewById(R.id.danwei);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode =1;
                set.setText("30");
                danwei.setText("MIN");
            }
        });
        dista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode =2;
                set.setText("5");
                danwei.setText("KM");
            }
        });
        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode =3;
                set.setText("FR");
                danwei.setText("EE");
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到另一个Activity
                Intent intent = new Intent(ModeChooseActivity.this, RunningActivity.class);
                intent.putExtra("mode",mode);
                intent.putExtra("data",set.getText());
                startActivity(intent);
            }
        });
    }
}