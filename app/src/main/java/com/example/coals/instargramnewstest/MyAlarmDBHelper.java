package com.example.coals.instargramnewstest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyAlarmDBHelper extends SQLiteOpenHelper {
    private static MyAlarmDBHelper sInstance;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "alarm.db";
    private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, " +
                            "%s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER)",
                    AlarmItemMemo.MemoEntry.TABLE_NAME,
                    AlarmItemMemo.MemoEntry._ID,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_TIME,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_MINUTE,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_SUNDAY,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_MONDAY,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_TUESDAY,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_WEDNESDAY,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_THURSDAY,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_FRIDAY,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_SATURDAY,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_MISSION,
                    AlarmItemMemo.MemoEntry.COLUMN_NAME_RINGTON

            );

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS "+AlarmItemMemo.MemoEntry.TABLE_NAME;


    public static MyAlarmDBHelper getsInstance(Context context){
        if(sInstance == null){
            sInstance = new MyAlarmDBHelper(context);
        }
        return sInstance;
    }

    //생성자 private 로 막아버림 -> 정적함수로 컨텍스트넣어주면 객체돌려주는형식?
    private MyAlarmDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }


    //한번도안쓴함수;;
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }




}
