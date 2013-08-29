package com.alarmclock.shake.app;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.WindowManager;
import android.widget.TextView;

import com.alarmclock.shake.app.services.ShakeService;
import com.alarmclock.shake.app.services.ShakeService.OnShakedListener;
import com.alarmclock.shake.app.services.ShakeService.ShakeServiceBinder;
import com.alarmclock.shake.app.utils.Utils;
import com.alarmclock.shake.app.widget.CircleProgressBar;

public class AlarmActivity extends Activity {

    private static final String LOG_TAG = "AlarmActivity";

    private TextView mCurrentTime;
    private CircleProgressBar mProgressBar;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final int UPDATE_CURRENT_TIME = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        new TimeThread().start();
        setContentView(R.layout.activity_alarm);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setupView();

        Intent shakeServiceIntent = new Intent(this, ShakeService.class);
        bindService(shakeServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void setupView() {
        mCurrentTime = (TextView)findViewById(R.id.current_time);
        mCurrentTime.setTypeface(Utils.getRobotoLightTypeface(this));
        mProgressBar = (CircleProgressBar)findViewById(R.id.circle_progressbar);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ShakeServiceBinder binder = (ShakeServiceBinder)service;
            ShakeService shakeService = binder.getService();
            shakeService.setOnShakedListener(new OnShakedListener() {

                @Override
                public void onShaked(int progress) {
                    if (progress >= 100) {
                        progress = 100;
                    }
                    mProgressBar.setProgress(progress);
                    if (progress == 100) {
                        finish();
                    }
                }
            });
        }
    };

    private class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Message msg = new Message();
                    msg.what = UPDATE_CURRENT_TIME;
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_CURRENT_TIME:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = TIME_FORMAT.format(sysTime);
                    mCurrentTime.setText(sysTimeStr);
                    break;

                default:
                    break;
            }
        }
    };

}
