package com.example.coals.instargramnewstest.AlarmCode;

public class AlarmData {
    private int hour;
    private int min;
    private boolean[] week;
    private int id;

    public AlarmData(int hour, int min, boolean[] week, int id) {
        this.hour = hour;
        this.min = min;
        this.week = week;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public boolean[] getWeek() {
        return week;
    }
}