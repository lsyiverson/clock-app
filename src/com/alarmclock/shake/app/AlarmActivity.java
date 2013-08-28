package com.alarmclock.shake.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.alarmclock.shake.app.services.ShakeService;
import com.alarmclock.shake.app.services.ShakeService.OnShakedListener;
import com.alarmclock.shake.app.services.ShakeService.ShakeServiceBinder;
import com.alarmclock.shake.app.widget.CircleProgressBar;

public class AlarmActivity extends Activity {

    private static final String LOG_TAG = "AlarmActivity";

    private CircleProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        setupView();

        Intent shakeServiceIntent = new Intent(this, ShakeService.class);
        bindService(shakeServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void setupView() {
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
                }
            });
        }
    };

}
