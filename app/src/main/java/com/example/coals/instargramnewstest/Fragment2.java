package com.example.coals.instargramnewstest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.coals.instargramnewstest.AlarmCode.AlarmReceiver;

import java.util.Calendar;


public class Fragment2 extends Fragment implements View.OnClickListener {
    public static int dbdb;


    private View view;
    //위젯변수
    private TextView tvAlarmLabel;
    public static TimePicker time_picker;
    public static Button btn_edit;
    public static ToggleButton[] toggleButtons = new ToggleButton[7];
    int[] toggleID = {R.id.tbSun, R.id.tbMon, R.id.tbTue, R.id.tbWen, R.id.tbThu, R.id.tbFri, R.id.tbSat};

    public static Button btn_start, btn_finish;
    //db 에 추가할 컬럼 변수명
    public int hour, min, sun, mon, tue, wen, thu, fri, sat, rington;
    public int mission = 0;
    public static RadioButton rb1, rb2, rb3;
    public static Calendar calendar;
    public static Intent alarmIntent;
    public static AlarmManager alarmManager;

    public static Fragment2 newInstance() {
        Fragment2 fragment2 = new Fragment2();
        return fragment2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmIntent = new Intent(MainActivity.context, AlarmReceiver.class);
        alarmManager = (AlarmManager) MainActivity.context.getSystemService(Context.ALARM_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);
        tvAlarmLabel = view.findViewById(R.id.tvAlarmLabel);
        time_picker = view.findViewById(R.id.time_picker);

        for (int i = 0; i < toggleButtons.length; i++) {
            toggleButtons[i] = view.findViewById(toggleID[i]);
        }
        rb1 = view.findViewById(R.id.rb1);
        rb2 = view.findViewById(R.id.rb2);
        rb3 = view.findViewById(R.id.rb3);
        btn_start = view.findViewById(R.id.btn_start);
        btn_finish = view.findViewById(R.id.btn_finish);
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_start.setOnClickListener(this);
        btn_finish.setOnClickListener(this);


        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        String msg;
        switch (view.getId()) {
            case R.id.btn_start:
                boolean[] week = new boolean[7];
                hour = time_picker.getHour();
                min = time_picker.getMinute();
                for (int i = 0; i < week.length; i++) {
                    week[i] = toggleButtons[i].isChecked();
                }
                if (week[0] == true) sun = 0;
                else sun = 1;
                if (week[1] == true) mon = 0;
                else mon = 1;
                if (week[2] == true) tue = 0;
                else tue = 1;
                if (week[3] == true) wen = 0;
                else wen = 1;
                if (week[4] == true) thu = 0;
                else thu = 1;
                if (week[5] == true) fri = 0;
                else fri = 1;
                if (week[6] == true) sat = 0;
                else sat = 1;
                msg = hour + " " + min + " " + sun + " " + mon + " " + tue + " " + wen + " " + thu + " " + fri + " " + sat + " ";
                Log.d("myData", msg);
                int music;
                if (rb1.isChecked()) {
                    rington = R.raw.teemo;
                    music = R.raw.teemo;
                } else if (rb2.isChecked()) {
                    rington = R.raw.yayou;
                    music = R.raw.yayou;
                } else {
                    rington = R.raw.army;
                    music = R.raw.army;
                }

                //요일 선택 했는지 체크
                int weekCheck = 0;
                for (int i = 0; i < week.length; i++) {
                    if (week[i] == false) {
                        weekCheck++;
                    }
                }
                if (weekCheck == 7) {
                    Toast.makeText(getContext(), "요일을 선택해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                //캘린더 시간 세팅
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, time_picker.getHour());
                calendar.set(Calendar.MINUTE, time_picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                // 시간 가져옴
                long timeCheck = (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) ?
                        calendar.getTimeInMillis() + AlarmManager.INTERVAL_HOUR :
                        calendar.getTimeInMillis();
                Log.d("Shake", "시간결과" + (calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()));


                //sql 저장하는 기본적인 방법은 컨텐트밸류즈라는 객체를 만들어서 거기에 담아서 저장가능
                //맵과같이 풋으로 키와밸류를저장할수있다.
                ContentValues contentValues = new ContentValues();
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_TIME, this.hour);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_MINUTE, min);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_SUNDAY, sun);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_MONDAY, mon);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_TUESDAY, tue);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_WEDNESDAY, wen);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_THURSDAY, thu);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_FRIDAY, fri);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_SATURDAY, sat);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_RINGTON, rington);
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_MISSION, mission);

                //db등록
                SQLiteDatabase db = MyAlarmDBHelper.getsInstance(getContext()).getWritableDatabase();
                long newRowId = db.insert(AlarmItemMemo.MemoEntry.TABLE_NAME, null, contentValues);
                if (newRowId == -1) {
                    Toast.makeText(getContext(), "저장에문제가발생하였습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(getContext(), "저장하였습니다.", Toast.LENGTH_SHORT).show();
                    db.close();
                }
                //위는 내가선택한알람설정 디비에 저장하기
                //밑은 시작버튼눌렀을때 프래그먼트1로 자동화면전환

                Fragment1.getMyDB();
                Fragment1.adapter.notifyDataSetChanged();



                //수정이다끝나고 1번째화면으로자동전환
                MainActivity.viewPager.setCurrentItem(0);


                alarmIntent.putExtra("music", music);
                alarmIntent.putExtra("state", "alarm on");
                alarmIntent.putExtra("week", week);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        timeCheck, AlarmManager.INTERVAL_DAY, getPendingIntent(getAlarmID(), alarmIntent));

                setInit();
                break;
            case R.id.btn_finish:
                setInit();
                break;


        }
    }

    public static PendingIntent getPendingIntent(int id, Intent intent) {


        return PendingIntent.getBroadcast(MainActivity.context, id, intent, AlarmManager.RTC_WAKEUP);
    }


    //설정 끝내고 나서 초기세팅하는 함수
    public static void setInit() {
        for (int i = 0; i < toggleButtons.length; i++) {
            toggleButtons[i].setChecked(false);

        }
        rb1.setChecked(true);

    }


    //DB에서 알람에 적용할 ID가져오는 함수
    public static int getAlarmID() {
        MyAlarmDBHelper dpHelper = MyAlarmDBHelper.getsInstance(MainActivity.context.getApplicationContext());
        Cursor c = dpHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM alarm ORDER BY " + AlarmItemMemo.MemoEntry._ID + " DESC;", null);
        int id = 0;
        int count = 0;
        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
            do {
                if (count == 1) {
                    return id;
                }
                id = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry._ID));


                Log.d("dd","id값 "+id);
                count++;

            } while (c.moveToNext());
            c.close();
            dpHelper.close();


        }
        return 0;

    }
}


