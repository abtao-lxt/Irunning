package com.example.runner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView rtime=(TextView) findViewById(R.id.rtime);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView dis=(TextView) findViewById(R.id.Dis);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView cal=(TextView) findViewById(R.id.Cal);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView chengjiu=(TextView) findViewById(R.id.chengjiu);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button ret=(Button) findViewById(R.id.return0);

        rtime.setText(Item.rtime);
        dis.setText(Item.distance+"KM");
        cal.setText(Item.calorie);

        String cj="";
        if(Double.parseDouble(Item.distance)<=2.0)
            cj="恭喜达成运动青铜成就";
        else if(Double.parseDouble(Item.distance)<=10.0)
            cj="恭喜达成运动黄金成就";
        else
            cj="恭喜达成运动王者成就";
        chengjiu.setText(cj);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到另一个Activity
                finish();
            }
        });
    }
}