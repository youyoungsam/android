package com.example.coals.instargramnewstest.AlarmCode;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.coals.instargramnewstest.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Shake", "onReceive");
        this.context = context;
        // intent로부터 전달받은 string
        String onOff = intent.getExtras().getString("state");
        boolean[] week = intent.getBooleanArrayExtra("week");
        int music=intent.getExtras().getInt("music");
        switch (music){
            case R.raw.teemo:
                Log.d("Shake","리시버음악은 티모");
                break;
            case R.raw.army:
                Log.d("Shake","리시버음악은 나팔");
                break;
            case R.raw.yayou:
                Log.d("Shake","리시버음악은 야유");
                break;
        }


        // AlarmService 서비스 intent 생성
        Calendar calendar = Calendar.getInstance();
        if (onOff.equals("alarm on")) {
            Log.d("Shake", "요일체크 :" + String.valueOf(week[(calendar.get(Calendar.DAY_OF_WEEK)) - 1]));
            if (!week[(calendar.get(Calendar.DAY_OF_WEEK)) - 1]) {
                return;
            }
        }

        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("music",music);

        Log.d("Shake", "onReceive Intent");
        // AlarmService extra string값 보내기
        serviceIntent.putExtra("state", onOff);
        // start the Alarm service
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            this.context.startForegroundService(serviceIntent);

        } else {
            this.context.startService(serviceIntent);
        }
    }
}
