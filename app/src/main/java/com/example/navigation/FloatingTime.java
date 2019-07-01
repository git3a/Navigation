package com.example.navigation;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Handler;
import android.os.Message;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Vibrator;
import android.media.MediaPlayer;

public class FloatingTime extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private Button button;

    private Integer i = 10;
    private Timer timer = null;
    private TimerTask task = null;
    private Vibrator vibrator;
    private MediaPlayer player;
    ProgressBar progressBar;
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 500;
        layoutParams.height = 200;
        layoutParams.x = 300;
        layoutParams.y = 300;

        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        player = MediaPlayer.create(FloatingTime.this, R.raw.ok);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //i = 10;

        Integer time = intent.getIntExtra("time", 0);
        i = time * 60;

        showFloatingWindow();
        startTime();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            button = new Button(getApplicationContext());
            button.setText(sdf.format(i * 1000));
            button.setBackgroundColor(Color.BLUE);
            button.setAlpha(0.6f);
            button.setTextColor(Color.WHITE);
            button.setTextSize(30);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i -= 1 * 60;
                }
            });
            windowManager.addView(button, layoutParams);
           // windowManager.addView(progressBar, layoutParams);

            button.setOnTouchListener(new FloatingOnTouchListener());
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //time.setText(msg.arg1 + "");
            //progressBar.setProgress(msg.arg1*10);
            button.setText(sdf.format(i * 1000));
            startTime();
        };
    };

    public void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (i > 0) {   //加入判断不能小于0
                    i--;
                    Message message = mHandler.obtainMessage();
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                } else if (i == 0) {
                    vibrator.vibrate(2000);
                    player.start();
                    closeFloat();
                    stopTime();
                    i--;
                }
            }
        };
        timer.schedule(task, 1000);
    }

    public void stopTime(){
        timer.cancel();
    }
    public void closeFloat() {
        windowManager.removeView(button);
        //Intent stopIntent = new Intent(this,FloatingTime.class);
       // stopService(stopIntent);
    }
}
