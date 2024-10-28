package com.example.runner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Calendar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

public class RunningActivity extends Activity implements LocationSource, AMapLocationListener {
    MapView mMapView = null;
    AMap aMap = null;
    private AMapLocation oldLocation;
//    List<LatLng> points = new ArrayList<LatLng>();
//    Polyline polyline;
    private List<LatLng> points = new ArrayList<>();  // 存储跑步路径的点
    private Polyline polyline;  // 用来绘制线条

    public int count = 0;
    private long recordingTime = 0;// 记录下来的总时间
    private double distan = 0.0;

    private double calor = 0.0;
    private boolean isrunning = true;
    private boolean isdraw = true;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private TextView Dis;
    private TextView Cal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        Button profileButton = findViewById(R.id.profile_button);  // 你的个人资料按钮
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确保这里的用户名是有效的，比如从登录信息中获取
                String currentUsername = "test_user";  // 替换为当前登录的用户名
                Intent intent = new Intent(RunningActivity.this, ProfileActivity.class);
                intent.putExtra("username", currentUsername);  // 将用户名传递给 ProfileActivity
                startActivity(intent);
            }
        });


        /*
        Intent intent = getIntent();
        if (intent != null) {
            // 假设我们传递的是一个字符串
            mode = intent.getIntExtra("mode",0);
            data=Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("data")));
        }*/

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }

        Dis=findViewById(R.id.Dis);
        Cal=findViewById(R.id.Cal);

        Chronometer mChronometer = findViewById(R.id.chronometer);
        if(Item.mode==1){
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
        }
        else{
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
        }

        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (Item.mode==1&&SystemClock.elapsedRealtime() - chronometer.getBase() >= Item.data*60*60) {

                    DatabaseHelper dbHelper = new DatabaseHelper(RunningActivity.this);
                    Chronometer mChronometer = findViewById(R.id.chronometer);
                    mChronometer.stop();
                    isrunning=false;
                    recordingTime=SystemClock.elapsedRealtime()-mChronometer.getBase();//getBase():返回时

                    AlertDialog.Builder builder = new AlertDialog.Builder(RunningActivity.this);
                    builder.setTitle("RUNNER")
                            .setMessage("目标已达成，是否继续运动")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mChronometer.setBase(SystemClock.elapsedRealtime()-recordingTime);
                                    mChronometer.start();
                                    isrunning=true;
                                    Item.mode=3;
                                    // 用户点击了确定按钮
                                }
                            })
                            .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Calendar calendar = Calendar.getInstance();
                                    int year = calendar.get(Calendar.YEAR);
                                    int month = calendar.get(Calendar.MONTH) + 1; // 注意：月份是从0开始的
                                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                                    int hour = calendar.get(Calendar.HOUR);
                                    int minute = calendar.get(Calendar.MINUTE);
                                    int second = calendar.get(Calendar.SECOND);
                                    String date1=year+"年"+month+"月"+day+"日";
                                    String date2=hour+"时"+minute+"分"+second+"秒";

                                    Date elapsedTimeDate = new Date(recordingTime);
                                    // 如果你需要以HH:MM:SS格式显示
                                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                                    String elapsedTimeFormatted = formatter.format(elapsedTimeDate);

                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("time", elapsedTimeFormatted);
                                    values.put("date1", date1);
                                    values.put("date2", date2);
                                    values.put("calorie", String.valueOf(calor));
                                    values.put("distance", String.valueOf(distan));
                                    db.insert("Running", null, values);
                                    db.close();

                                    Item.rtime=elapsedTimeFormatted;
                                    Item.distance=String.valueOf(distan);
                                    Item.calorie=String.valueOf(calor);

                                    Intent intent = new Intent(RunningActivity.this, ResultActivity.class);
                                    startActivity(intent);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        Button pause=(Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.stop();
                isrunning=false;
                recordingTime=SystemClock.elapsedRealtime()-mChronometer.getBase();//getBase():返回时
                AlertDialog.Builder builder = new AlertDialog.Builder(RunningActivity.this);
                builder.setTitle("RUNNER")
                        .setMessage("运动已暂停")
                        .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mChronometer.setBase(SystemClock.elapsedRealtime()-recordingTime);
                                mChronometer.start();
                                isrunning=true;
                                // 用户点击了确定按钮
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Button finish=(Button) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.stop();
                isrunning=false;
                recordingTime=SystemClock.elapsedRealtime()-mChronometer.getBase();//getBase():返回时

                AlertDialog.Builder builder = new AlertDialog.Builder(RunningActivity.this);
                builder.setTitle("RUNNER")
                        .setMessage("是否结束跑步")
                        .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mChronometer.setBase(SystemClock.elapsedRealtime()-recordingTime);
                                mChronometer.start();
                                isrunning=true;
                                // 用户点击了确定按钮
                            }
                        })
                        .setNegativeButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1; // 注意：月份是从0开始的
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int hour = calendar.get(Calendar.HOUR);
                                int minute = calendar.get(Calendar.MINUTE);
                                int second = calendar.get(Calendar.SECOND);
                                String date1=year+"年"+month+"月"+day+"日";
                                String date2=hour+"时"+minute+"分"+second+"秒";

                                Date elapsedTimeDate = new Date(recordingTime);
                                // 如果你需要以HH:MM:SS格式显示
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                                String elapsedTimeFormatted = formatter.format(elapsedTimeDate);

                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("time", elapsedTimeFormatted);
                                values.put("date1", date1);
                                values.put("date2", date2);
                                values.put("calorie", String.valueOf(calor));
                                values.put("distance", String.valueOf(distan));
                                db.insert("Running", null, values);
                                db.close();

                                Item.rtime=elapsedTimeFormatted;
                                Item.distance=String.valueOf(distan);
                                Item.calorie=String.valueOf(calor);

                                Intent intent = new Intent(RunningActivity.this, ResultActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        init();

    }

    private void updateUserTotalDistance(String username, double currentDistance) {
        DatabaseHelper dbHelper = new DatabaseHelper(RunningActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 检查用户是否已存在于 UserTotalDistance 表中
        Cursor cursor = db.rawQuery("SELECT total_distance FROM UserTotalDistance WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            // 检查列索引
            int columnIndex = cursor.getColumnIndex("total_distance");
            if (columnIndex != -1) {
                double previousTotal = cursor.getDouble(columnIndex);
                double newTotal = previousTotal + currentDistance;
                db.execSQL("UPDATE UserTotalDistance SET total_distance = ? WHERE username = ?", new Object[]{newTotal, username});
            } else {
                Log.e("RunningActivity", "列 'total_distance' 不存在");
            }
        } else {
            // 如果用户不存在，则插入新的记录
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("total_distance", currentDistance);
            db.insert("UserTotalDistance", null, values);
        }
        cursor.close();
        db.close();
    }

    private  void init(){
        if (aMap == null) {

            aMap = mMapView.getMap();

        }
        //设置amap的一些属性
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);// 设置指南针是否显示
        uiSettings.setZoomControlsEnabled(true);// 设置缩放按钮是否显示
        uiSettings.setScaleControlsEnabled(true);// 设置比例尺是否显示
        uiSettings.setRotateGesturesEnabled(true);// 设置地图旋转是否可用
        uiSettings.setTiltGesturesEnabled(false);// 设置地图倾斜是否可用
        uiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。

        myLocationStyle.radiusFillColor(Color.TRANSPARENT);  // 隐藏蓝色精度圈
        myLocationStyle.strokeColor(Color.TRANSPARENT);  // 隐藏边框
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位 LOCATION_TYPE_LOCATE、跟随 LOCATION_TYPE_MAP_FOLLOW 或地图根据面向方向旋转 LOCATION_TYPE_MAP_ROTATE
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);

        // 缩放级别（zoom）：值越大地图越详细(4-20)
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        //使用 aMap.setMapTextZIndex(2) 可以将地图底图文字设置在添加的覆盖物之上
        aMap.setMapTextZIndex(2);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Log.e("Amap", amapLocation.getLatitude() + "," + amapLocation.getLongitude());
                LatLng newLatLng =  new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                if(isrunning==true){
                    points.add(count, newLatLng);
                    count++;
                    drawLines();
                    if(isdraw==true&&count!=1){
                        distan+= AMapUtils.calculateLineDistance(points.get(count),points.get(count-1));

                        Dis.setText(String.valueOf(distan));
                        calor=60*distan*1.036;//默认体重为60kg
                        Cal.setText(String.valueOf(calor));

                        if (Item.mode==2&&distan>=Item.data){

                            DatabaseHelper dbHelper = new DatabaseHelper(this);
                            Chronometer mChronometer = findViewById(R.id.chronometer);
                            mChronometer.stop();
                            isrunning=false;
                            recordingTime=SystemClock.elapsedRealtime()-mChronometer.getBase();//getBase():返回时

                            AlertDialog.Builder builder = new AlertDialog.Builder(RunningActivity.this);
                            builder.setTitle("RUNNER")
                                    .setMessage("目标已达成，是否继续运动")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mChronometer.setBase(SystemClock.elapsedRealtime()-recordingTime);
                                            mChronometer.start();
                                            isrunning=true;
                                            Item.mode=3;
                                            // 用户点击了确定按钮
                                        }
                                    })
                                    .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Calendar calendar = Calendar.getInstance();
                                            int year = calendar.get(Calendar.YEAR);
                                            int month = calendar.get(Calendar.MONTH) + 1; // 注意：月份是从0开始的
                                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                                            int hour = calendar.get(Calendar.HOUR);
                                            int minute = calendar.get(Calendar.MINUTE);
                                            int second = calendar.get(Calendar.SECOND);
                                            String date1=year+"年"+month+"月"+day+"日";
                                            String date2=hour+"时"+minute+"分"+second+"秒";

                                            Date elapsedTimeDate = new Date(recordingTime);
                                            // 如果你需要以HH:MM:SS格式显示
                                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                                            String elapsedTimeFormatted = formatter.format(elapsedTimeDate);

                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                            ContentValues values = new ContentValues();
                                            values.put("time", elapsedTimeFormatted);
                                            values.put("date1", date1);
                                            values.put("date2", date2);
                                            values.put("calorie", String.valueOf(calor));
                                            values.put("distance", String.valueOf(distan));
                                            db.insert("Running", null, values);
                                            db.close();

                                            Item.rtime=elapsedTimeFormatted;
                                            Item.distance=String.valueOf(distan);
                                            Item.calorie=String.valueOf(calor);

                                            Intent intent = new Intent(RunningActivity.this, ResultActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    isdraw=true;
                }
                else{
                    isdraw=false;
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 绘制路线
     */
    public void drawLines() {

        PolylineOptions options = new PolylineOptions();
        options.geodesic(true)  // 使用地理线条
                .setDottedLine(false)  // 禁用虚线，确保路径为实线
                .color(Color.RED)  // 设置路径颜色为红色
                .addAll(points)  // 添加所有的路径点
                .width(30)  // 设置线条宽度为15，使路径更粗
                .visible(true);  // 确保路径可见

        polyline = aMap.addPolyline(options);  // 在地图上绘制路径

        // 获取轨迹坐标点，设置地图视野
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < points.size(); i++) {
            b.include(points.get(i));  // 将所有路径点包含在视野中
        }
        LatLngBounds bounds = b.build();

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 100);  // 调整地图视野到包含所有路径点
        aMap.animateCamera(update);  // 动画效果更新视野
    }



    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            try {
                mlocationClient = new AMapLocationClient(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener((AMapLocationListener) this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(false);
            mLocationOption.setGpsFirst(true);
            // 设置发送定位请求的时间间隔,最小值为1000ms,1秒更新一次定位信息
            mLocationOption.setInterval(10000);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationOption.setMockEnable(true);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {

    }
}

