package com.alarmclock.shake.app.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class ShakeService extends Service implements SensorEventListener{

    private static final String LOG_TAG = "ShakeService";

    private SensorManager mSensorManager;

    private Sensor mAccelerometerSensor;

    private IBinder mBinder = new ShakeServiceBinder();

    private OnShakedListener mShakedListener;

    public interface OnShakedListener {
        public void onShaked(int progress);
    }

    public void setOnShakedListener(OnShakedListener listener) {
        mShakedListener = listener;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unregisterSensorListener();
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        registerSensorListener();
        return mBinder;
    }

    private void registerSensorListener() {
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterSensorListener() {
        if (mAccelerometerSensor != null) {
            mSensorManager.unregisterListener(this, mAccelerometerSensor);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    int count = 0;
    int n = 14;
    int level = 1;
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:

                float x = Math.abs(event.values[0]);
                float y = Math.abs(event.values[1]);
                float z = Math.abs(event.values[2]);
                if(x > n || y > n || z > n) {
                    float sum = 0;
                    sum = (x-n) > 0?sum+x-n:sum;
                    sum = (y-n) > 0?sum+y-n:sum;
                    sum = (z-n) > 0?sum+z-n:sum;
                    count += level;
                    if (null != mShakedListener) {
                        mShakedListener.onShaked(count);
                    }
                }
        }
    }

    public class ShakeServiceBinder extends Binder {
        public ShakeService getService() {
            return ShakeService.this;
        }
    }

}
