package com.example.runner;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<record> recordsList = new ArrayList<>();

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Running", null);

        mRecyclerView = findViewById(R.id.rv_list);

        if(cursor.moveToFirst()){
            do{
                record records = new record();
                records.time1 = cursor.getString(cursor.getColumnIndex("date1"));
                records.time2 = cursor.getString(cursor.getColumnIndex("date2"));
                records.rtime = cursor.getString(cursor.getColumnIndex("time"));
                records.cal = cursor.getString(cursor.getColumnIndex("calorie"));
                records.dis = cursor.getString(cursor.getColumnIndex("distance"))+"KM";
                recordsList.add(records);
            }while(cursor.moveToNext());
        }

        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RecordsActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        //绑定item布局
        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(RecordsActivity.this, R.layout.layout_message, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        //给item添加数据
        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            record records = recordsList.get(position);
            holder.time1.setText(records.time1);
            holder.time2.setText(records.time2);
            holder.rtime.setText(records.rtime);
            holder.cal.setText(records.cal);
            holder.dis.setText(records.dis);
        }

        //设置item条数
        @Override
        public int getItemCount() {
            return recordsList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView rtime;
        TextView time1;
        TextView time2;
        TextView cal;
        TextView dis;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            rtime = itemView.findViewById(R.id.rtime);
            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);
            dis = itemView.findViewById(R.id.Dis);
            cal = itemView.findViewById(R.id.Cal);
        }
    }
}