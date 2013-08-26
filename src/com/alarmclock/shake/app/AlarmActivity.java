package com.alarmclock.shake.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ProgressBar;

import com.alarmclock.shake.app.services.ShakeService;
import com.alarmclock.shake.app.services.ShakeService.OnShakedListener;
import com.alarmclock.shake.app.services.ShakeService.ShakeServiceBinder;

public class AlarmActivity extends Activity {

    private static final String LOG_TAG = "AlarmActivity";

    private ProgressBar mProgressBar;

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
        mProgressBar = (ProgressBar)findViewById(R.id.progress);
        mProgressBar.setIndeterminate(true);
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
                    mProgressBar.setIndeterminate(false);
                    mProgressBar.setProgress(progress);
                }
            });
        }
    };

}
