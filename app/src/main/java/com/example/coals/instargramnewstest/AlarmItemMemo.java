package com.example.coals.instargramnewstest;

import android.provider.BaseColumns;

public class AlarmItemMemo {
    //유지보수 편하게 하려고 만든 상수 클래스

    private AlarmItemMemo(){

    }

    public static  class MemoEntry implements BaseColumns{
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_MINUTE = "min";
        public static final String COLUMN_NAME_SUNDAY = "Sunday";
        public static final String COLUMN_NAME_MONDAY = "Monday";
        public static final String COLUMN_NAME_TUESDAY = "Tuesday";
        public static final String COLUMN_NAME_WEDNESDAY = "Wednesday";
        public static final String COLUMN_NAME_THURSDAY = "Thursday";
        public static final String COLUMN_NAME_FRIDAY = "Friday";
        public static final String COLUMN_NAME_SATURDAY = "Saturday";
        public static final String COLUMN_NAME_MISSION ="mission";
        public static final String COLUMN_NAME_RINGTON ="rington";

    }
}
