package com.example.coals.instargramnewstest;

public class ItemData {
    private String tvTime;
    private String tvWeek;
    private String tvAmpm;
    private int tvItemPri;
    private int music;
    private int mission;

    public ItemData(String tvTime, String tvWeek, String tvAmpm, int tvItemPri, int music, int mission) {
        this.tvTime = tvTime;
        this.tvWeek = tvWeek;
        this.tvAmpm = tvAmpm;
        this.tvItemPri = tvItemPri;
        this.music = music;
        this.mission = mission;
    }

    public int getMission() {
        return mission;
    }

    public int getMusic() {
        return music;
    }

    public String getTvTime() {
        return tvTime;
    }

    public void setTvTime(String tvTime) {
        this.tvTime = tvTime;
    }

    public String getTvWeek() {
        return tvWeek;
    }

    public void setTvWeek(String tvWeek) {
        this.tvWeek = tvWeek;
    }

    public String getTvAmpm() {
        return tvAmpm;
    }

    public void setTvAmpm(String tvAmpm) {
        this.tvAmpm = tvAmpm;
    }

    public int getTvItemPri() {
        return tvItemPri;
    }

    public void setTvItemPri(int tvItemPri) {
        this.tvItemPri = tvItemPri;
    }
}
