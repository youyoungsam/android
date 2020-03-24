package com.example.coals.instargramnewstest.AlarmCode;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coals.instargramnewstest.Fragment2;
import com.example.coals.instargramnewstest.MainActivity;
import com.example.coals.instargramnewstest.R;

public class StartAlarm extends Activity implements SensorEventListener {

    //미션창 & 자이로센서 변수
    public static TextView tv_label;
    Button btn_main;
    public static Intent intent;
    private Sensor accelerormeterSensor;
    private SensorManager sensorManager;
    private long lastTime;
    private float speed,x,y,z,lastX,lastY,lastZ;
    public static int count = 0;
    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;
    private int num;
    public static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity_alarm);
        //미션창으로 넘어오는 인텐트
        intent=getIntent();


        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        tv_label=findViewById(R.id.tv_label);
        btn_main=findViewById(R.id.btn_startMain);


        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                    MainActivity.viewPager.setCurrentItem(0);
            }
        });

        btn_main.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //흔들기 감지
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            //흔들기 감지 시간텀
            if (gabOfTime > 300) {
                lastTime = currentTime;
                x = sensorEvent.values[SensorManager.DATA_X];
                y = sensorEvent.values[SensorManager.DATA_Y];
                z = sensorEvent.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;
                //흔들기 속도 감지 (알람이 울렸을 때)
                if (speed > SHAKE_THRESHOLD&&AlarmService.isRunning==true) {
                    // doSomething
                    count++;
                    num=count;
                    Toast.makeText(this, String.valueOf(count++), Toast.LENGTH_SHORT).show();
                    tv_label.setText(String.valueOf(num)+"번흔드셨습니다");
                    btn_main.setVisibility(View.INVISIBLE);
                    if (count == 20) {
                        //20회일 때 알람 정지 및 미션창 빠져 나갈 수 있게 버튼 활성화
                        Fragment2.alarmIntent.putExtra("state", "alarm off");
                        MainActivity.context.sendBroadcast(Fragment2.alarmIntent);
                        count = 0;
                        num=count;
                        tv_label.setText("완료");
                        btn_main.setVisibility(View.VISIBLE);
                    }
                }

                lastX = sensorEvent.values[DATA_X];
                lastY = sensorEvent.values[DATA_Y];
                lastZ = sensorEvent.values[DATA_Z];
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        Log.d("Shake", "스타트");

    }


    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
        Log.d("Shake", "스탑");
    }









    //볼륨버튼막기
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            Toast.makeText(this,"볼륨을내릴수없습니다.",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(this,"볼륨을올릴수없습니다.",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    //뒤로가기 키 막기
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "back키 사용불가.", Toast.LENGTH_SHORT).show();
        return; // 그냥 끝내기
    }


    //메뉴키 막는 방법
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(),0);
        Toast.makeText(this, "메뉴키 사용불가.", Toast.LENGTH_SHORT).show();
    }

    // 홈키 재실행 방식으로 막기
//    @Override
//    public void onStop() {
//        super.onStop();        //if() { // 여기서 종료시간을 캐치해서 종료 되게 만드는 것이다.//
//        startActivity(new Intent(this, MainActivity.class));
//        Toast.makeText(this, "재실행", Toast.LENGTH_SHORT).show();
//    }




}
