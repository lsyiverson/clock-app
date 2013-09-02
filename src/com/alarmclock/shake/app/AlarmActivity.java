package com.alarmclock.shake.app;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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

import com.alarmclock.shake.app.model.ShakeAlarmClock;
import com.alarmclock.shake.app.services.ShakeService;
import com.alarmclock.shake.app.services.ShakeService.OnShakedListener;
import com.alarmclock.shake.app.services.ShakeService.ShakeServiceBinder;
import com.alarmclock.shake.app.utils.Constants;
import com.alarmclock.shake.app.utils.Utils;
import com.alarmclock.shake.app.widget.CircleProgressBar;

public class AlarmActivity extends Activity {

    private static final String LOG_TAG = "AlarmActivity";

    private TextView mCurrentTime;
    private CircleProgressBar mProgressBar;

    private static final int UPDATE_CURRENT_TIME = 0x01;

    /*
     * The alarm will last one minute and stopped
     */
    private static final int MINUTE = 60000;

    private Handler mAlarmTimeOutHandler;

    private ShakeAlarmClock mAlarm;

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

        mAlarm = (ShakeAlarmClock)getIntent().getSerializableExtra(Constants.ALARM_RAW_DATA);

        Intent shakeServiceIntent = new Intent(this, ShakeService.class);
        shakeServiceIntent.putExtra(Constants.ALARM_RAW_DATA, mAlarm);
        bindService(shakeServiceIntent, mConnection, Context.BIND_AUTO_CREATE);

        mAlarmTimeOutHandler = new Handler();
        mAlarmTimeOutHandler.postDelayed(ScheduleNextRunnable, MINUTE);
    }

    private Runnable ScheduleNextRunnable = new Runnable() {

        @Override
        public void run() {
            AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Calendar calendar=Calendar.getInstance();
            // Schedule a alarm after 5 minutes if the user won't stop the alarm in one minute
            calendar.setTimeInMillis(System.currentTimeMillis() + 5*MINUTE);

            Date date = calendar.getTime();

            mAlarm.setTime(Utils.TIME_FORMAT.format(date));

            Intent intent=new Intent(AlarmActivity.this, AlarmActivity.class);
            intent.putExtra(Constants.ALARM_RAW_DATA, mAlarm);

            PendingIntent pendingIntent=PendingIntent.getActivity(AlarmActivity.this, mAlarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            finish();
        }
    };

    private void setupView() {
        mCurrentTime = (TextView)findViewById(R.id.current_time);
        mCurrentTime.setTypeface(Utils.getRobotoLightTypeface(this));
        mProgressBar = (CircleProgressBar)findViewById(R.id.circle_progressbar);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        mAlarmTimeOutHandler.removeCallbacks(ScheduleNextRunnable);
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
                    mAlarmTimeOutHandler.removeCallbacks(ScheduleNextRunnable);
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
                    CharSequence sysTimeStr = Utils.TIME_FORMAT.format(sysTime);
                    mCurrentTime.setText(sysTimeStr);
                    break;

                default:
                    break;
            }
        }
    };

}
