package com.example.coals.instargramnewstest.AlarmCode;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.coals.instargramnewstest.R;


public class AlarmService extends Service {

    AudioManager audioManager;
    static MediaPlayer mediaPlayer;
    Vibrator vibrator;
    public static boolean isRunning = false;
    long[] timings = new long[]{0, 100, 0, 400, 0, 200, 0, 400};
    int[] amplitudes =new  int[]{0, 50, 0, 100, 0, 50, 0, 150};
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Shake", "onBind");

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        Log.d("Shake", "RingService onCreate");
        String CHANNEL_ID = "default";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("미션알람")
                .setContentText("설정된 시간에 알람이 재생 됩니다")
                .setSmallIcon(R.drawable.priet)
                .build();

        startForeground(1, notification);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Shake", "onStartCommand");
        String getState = intent.getExtras().getString("state");
        Log.d("Shake", "값들어가나 " + getState);
        Log.d("Shake", "불리언 값들어가나 " + String.valueOf(this.isRunning));
        int music=intent.getExtras().getInt("music");
        switch (music){
            case R.raw.teemo:
                Log.d("Shake","음악은 티모");
                break;
            case R.raw.army:
                Log.d("Shake","음악은 나팔");
                break;
            case R.raw.yayou:
                Log.d("Shake","음악은 야유");
                break;
        }
        assert getState != null;
        switch (getState) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying() == false && startId == 1) {
                Log.d("Shake", "미디아 플레이아 재생");
                mediaPlayer.setLooping(true);
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, 1));
                mediaPlayer.start();
                mediaPlayer.setVolume(1.0f,1.0f);
                isRunning=true;
            } else if (mediaPlayer.isPlaying()==true&&startId == 0) {
                Log.d("Shake", "미디아 플레이아 정지");
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
                isRunning=false;
                vibrator.cancel();
                vibrator=null;
            }
        }else{
            if(isRunning==false&&startId==1){
                Log.d("Shake", "미디아 플레이아 재생");
                vibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, 1));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_PLAY_SOUND);

                mediaPlayer = MediaPlayer.create(this, music);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                isRunning=true;

                // 알림창 호출
                Intent sIntent = new Intent(this, StartAlarm.class);
//        // 새로운 TASK를 생성해서 Activity를 최상위로 올림
                sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(sIntent);



            }else if(isRunning==true&&startId==0){
                Log.d("Shake", "미디아 플레이아 정지");
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
                isRunning=false;
                vibrator.cancel();
                vibrator=null;

            }



        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("Shake", "서비스 파괴");

    }





}
