package com.example.coals.instargramnewstest;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment1 extends Fragment {

    private View view;
    //위젯변수
    private TextView txtNextAlarmTime;//리사이클러뷰 아이템에서 시간가져와서 다음알람시간언젠지 알려주는 텍뷰 구현x
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recycler;
    //리사이클뷰아이템
    public static ArrayList<ItemData> arrayList = new ArrayList<>();
    static int hour;
    static int min;
    int sun; // sum ~ sat == db에서 값가져와서 0이면참 1이면 거짓 조건걸어서 set_week 에 ex) 월,수,금 이런식으로 셋팅
    int mon;
    int tue;
    int wen;
    int thu;
    int fri;
    int sat;
    static int mission;
    static int rington;
    static String set_time;
    String set_week;
    static String set_ampm;
    static int set_primary;
    static int[] weekday = new int[7];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment1, container, false);
        txtNextAlarmTime = view.findViewById(R.id.txtNextAlarmTime);
        recycler = view.findViewById(R.id.recyclerviewAlarm);
        //리사이클러뷰 기존성능 강화
        recycler.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);



        adapter = new RecyclerAdapter(arrayList, getContext());
        getMyDB();
        ((RecyclerAdapter) adapter).setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(View v, int position) {
                MainActivity.viewPager.setCurrentItem(1);
                //선택한아이템에프라이머리키가져오기성공
                Log.d("prime", String.valueOf(arrayList.get(position).getTvItemPri()));
                final int p = arrayList.get(position).getTvItemPri();


                MyAlarmDBHelper dpHelper = MyAlarmDBHelper.getsInstance(getActivity().getApplicationContext());
                Cursor c = dpHelper.getReadableDatabase()
                        .rawQuery("SELECT * FROM alarm WHERE _ID='" + p + "';", null);
                int[] db = new int[7];
                int dbtime, dbmin;
                int dbMusic;
                if (c != null && c.getCount() != 0) {
                    c.moveToFirst();
                    do {
                        dbtime = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_TIME));
                        dbmin = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_MINUTE));
                        db[0] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_SUNDAY));
                        db[1] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_MONDAY));
                        db[2] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_TUESDAY));
                        db[3] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_WEDNESDAY));
                        db[4] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_THURSDAY));
                        db[5] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_FRIDAY));
                        db[6] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_SATURDAY));
                        dbMusic=c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_RINGTON));
                    } while (c.moveToNext());
                    c.close();
                    dpHelper.close();

                    switch (dbMusic){
                        case R.raw.teemo:
                            Fragment2.rb1.setChecked(true);
                        case R.raw.yayou:
                            Fragment2.rb2.setChecked(true);
                        case R.raw.army:
                            Fragment2.rb3.setChecked(true);


                    }


                    Log.d("min", String.valueOf(dbmin));
                    Fragment2.time_picker.setHour(dbtime);
                    Fragment2.time_picker.setMinute(dbmin);
                    boolean[] resultweek = new boolean[7];
                    for (int i = 0; i < db.length; i++) {
                        if (db[i] == 0) {
                            resultweek[i] = true;
                        } else {
                            resultweek[i] = false;
                        }

                    }
                    for (int i = 0; i < Fragment2.toggleButtons.length; i++) {
                        Fragment2.toggleButtons[i].setChecked(resultweek[i]);
                    }
                }//end of if(c.getCount())
                //나가기버튼
                Fragment2.btn_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //취소버튼누르면 프래그먼트2에있는 시작버튼활성화하고 수정버튼은비활성화해야됨
                        //취소버튼말고 메인 뷰페이퍼 클릭해서 나갈수도있으니 이부분은 메인에서도 시작,수정버튼 활성,비활성해서 막야함
                        MainActivity.viewPager.setCurrentItem(0);
                        Fragment2.btn_start.setVisibility(View.VISIBLE);
                        Fragment2.btn_edit.setVisibility(View.INVISIBLE);
                    }
                });
                Fragment2.btn_start.setVisibility(View.INVISIBLE);
                Fragment2.btn_edit.setVisibility(View.VISIBLE);
                Fragment2.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //수정버튼활성화하고 이벤트처리
                        //수정한요일 true 면 0 false 면 1로 바꿔주기
                        //요일 선택 했는지 체크
                        int weekCheck = 0;
                        boolean[] week=new boolean[7];
                        int[] setdbweek = new int[7];
                        if (Fragment2.toggleButtons[0].isChecked() == true) {
                            setdbweek[0] = 0;
                        } else {
                            setdbweek[0] = 1;
                            weekCheck++;
                        }
                        if (Fragment2.toggleButtons[1].isChecked() == true) {
                            setdbweek[1] = 0;
                        } else {
                            setdbweek[1] = 1;
                            weekCheck++;
                        }
                        if (Fragment2.toggleButtons[2].isChecked() == true) {
                            setdbweek[2] = 0;
                        } else {
                            setdbweek[2] = 1;
                            weekCheck++;
                        }
                        if (Fragment2.toggleButtons[3].isChecked() == true) {
                            setdbweek[3] = 0;
                        } else {
                            setdbweek[3] = 1;
                            weekCheck++;
                        }
                        if (Fragment2.toggleButtons[4].isChecked() == true) {
                            setdbweek[4] = 0;
                        } else {
                            setdbweek[4] = 1;
                            weekCheck++;
                        }
                        if (Fragment2.toggleButtons[5].isChecked() == true) {
                            setdbweek[5] = 0;
                        } else {
                            setdbweek[5] = 1;
                            weekCheck++;
                        }
                        if (Fragment2.toggleButtons[6].isChecked() == true) {
                            setdbweek[6] = 0;
                        } else {
                            setdbweek[6] = 1;
                            weekCheck++;
                        }
                        for(int i=0;i<Fragment2.toggleButtons.length;i++){
                            week[i]=Fragment2.toggleButtons[i].isChecked();
                        }



                        if (weekCheck == 7) {
                            Toast.makeText(getContext(), "요일을 선택해 주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int music=0;
                        if (Fragment2.rb1.isChecked()) {
                            rington = R.raw.teemo;
                            music= R.raw.teemo;
                        } else if (Fragment2.rb2.isChecked()) {
                            rington = R.raw.yayou;
                            music= R.raw.yayou;
                        } else {
                            rington = R.raw.army;
                            music= R.raw.army;
                        }







                        //sql 저장하는 기본적인 방법은 컨텐트밸류즈라는 객체를 만들어서 거기에 담아서 저장가능
                        //맵과같이 풋으로 키와밸류를저장할수있다.
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_TIME, Fragment2.time_picker.getHour());
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_MINUTE, Fragment2.time_picker.getMinute());
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_SUNDAY, setdbweek[0]);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_MONDAY, setdbweek[1]);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_TUESDAY, setdbweek[2]);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_WEDNESDAY, setdbweek[3]);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_THURSDAY, setdbweek[4]);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_FRIDAY, setdbweek[5]);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_SATURDAY, setdbweek[6]);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_MISSION, 0);
                        contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_RINGTON, rington);

                        //db수정등록
                        SQLiteDatabase db = MyAlarmDBHelper.getsInstance(getContext()).getWritableDatabase();
                        long newRowId = db.update(AlarmItemMemo.MemoEntry.TABLE_NAME, contentValues, "_id=" + p, null);
                        if (newRowId == -1) {
                            Toast.makeText(getContext(), "수정에문제가발생하였습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "수정하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        db.close();




                        if (arrayList != null) {
                            //캘린더 시간 세팅
                            Fragment2.calendar=Calendar.getInstance();
                            Fragment2.calendar.set(Calendar.HOUR_OF_DAY, Fragment2.time_picker.getHour());
                            Fragment2.calendar.set(Calendar.MINUTE, Fragment2.time_picker.getMinute());
                            Fragment2.calendar.set(Calendar.SECOND, 0);
                            // 시간 가져옴
                            long timeCheck=(Fragment2.calendar.getTimeInMillis()<Calendar.getInstance().getTimeInMillis())?
                                    Fragment2. calendar.getTimeInMillis()+AlarmManager.INTERVAL_HOUR:
                                    Fragment2.calendar.getTimeInMillis();





                            Fragment2.alarmIntent.putExtra("music",music);
                            Fragment2.alarmIntent.putExtra("state", "alarm on");
                            Fragment2.alarmIntent.putExtra("week", week);
                            Fragment2.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    timeCheck, AlarmManager.INTERVAL_DAY, Fragment2.getPendingIntent(p, Fragment2.alarmIntent));

                            arrayList.removeAll(arrayList);
                        }
                        getMyDB();
//                        ((RecyclerAdapter) adapter).setArrayList(arrayList);
                        adapter.notifyDataSetChanged();
                        //수정이다끝나고 1번째화면으로자동전환
                        MainActivity.viewPager.setCurrentItem(0);
                        Fragment2.setInit();
                    }
                });
            }
        });//end of click event recyclerviewItem

        ((RecyclerAdapter) adapter).setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, final int position) {
                final int deletePri = arrayList.get(position).getTvItemPri();

                Toast.makeText(getContext(), "롱클릭값=" + position + "검색된프라이머리키값=" + String.valueOf(arrayList.get(position).getTvItemPri()), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("알람삭제");
                builder.setMessage("알람을 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //삭제이벤트
                        SQLiteDatabase db = MyAlarmDBHelper.getsInstance(getContext()).getWritableDatabase();

                        long deleteRowId = db.delete(AlarmItemMemo.MemoEntry.TABLE_NAME, AlarmItemMemo.MemoEntry._ID + "=" +
                                deletePri, null);
                        if (deleteRowId == 0) {
                            Toast.makeText(getContext(), "삭제에문제가발생하였습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                        if (arrayList != null) {
                            Fragment2.alarmManager.cancel(Fragment2.getPendingIntent(deletePri, Fragment2.alarmIntent));
                            Fragment2.getPendingIntent(deletePri, Fragment2.alarmIntent).cancel();

                            arrayList.removeAll(arrayList);
                        }
                        getMyDB();
                        adapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();


            }
        });


        adapter.notifyDataSetChanged();
        //Adapter에게 Date set의 각 item들이 자신만의 고유한 값을 가지고있다는 알려주는 기능
        adapter.setHasStableIds(true);
        recycler.setAdapter(adapter);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    public static void getMyDB() {
//        if(arrayList != null) arrayList=null;


        MyAlarmDBHelper dpHelper = MyAlarmDBHelper.getsInstance(MainActivity.context);
        Cursor cursor = dpHelper.getReadableDatabase()
                //모든데이터를가져오고싶으면 모든조건에 null을준다.
                .query(AlarmItemMemo.MemoEntry.TABLE_NAME,
                        null, null, null, null, null, null);
        arrayList.removeAll(arrayList);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                hour = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_TIME));
                min = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_MINUTE));
                weekday[0] = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_SUNDAY));
                weekday[1] = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_MONDAY));
                weekday[2] = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_TUESDAY));
                weekday[3] = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_WEDNESDAY));
                weekday[4] = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_THURSDAY));
                weekday[5] = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_FRIDAY));
                weekday[6] = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_SATURDAY));
                mission = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_MISSION));
                rington = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_RINGTON));
                //프라이머리키셋팅
                set_primary = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry._ID));
                Log.d("ggg", "프라이머리키값:" + String.valueOf(set_primary));

                //오전오후셋팅
                if (hour >= 0 && hour <= 12) {
                    set_ampm = "오전";
                } else {
                    set_ampm = "오후";
                }
                //시간+분 셋팅
                String setMin = "";
                if (hour > 12) {
                    hour -= 12;
                }
                set_time = String.valueOf(hour) + " : ";
                if (min < 10) {
                    set_time = set_time + String.valueOf(0) + String.valueOf(min);
                } else {
                    set_time = set_time + String.valueOf(min);
                }

                String[] weekk = {"일", "월", "화", "수", "목", "금", "토"};
                String result = "";
                for (int i = 0; i < weekday.length; i++) {
                    if (weekday[i] == 0) {
                        result += weekk[i] + " ";
                    }
                }


                //어레이리스트에 읽어온 쿼리결과물 1줄 저장
                arrayList.add(new ItemData(set_time, result, set_ampm, set_primary, rington, mission));

            } while (cursor.moveToNext());

        }
        cursor.close();
        dpHelper.close();
        adapter.notifyDataSetChanged();
    }

}

