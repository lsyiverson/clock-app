package com.alarmclock.shake.app.services;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alarmclock.shake.app.model.ShakeAlarmClock;
import com.alarmclock.shake.app.utils.Constants;
import com.alarmclock.shake.app.utils.Utils;

public class ShakeService extends Service implements SensorEventListener{

    private static final String LOG_TAG = "ShakeService";

    private SensorManager mSensorManager;

    private Sensor mAccelerometerSensor;

    private Vibrator mVibrator;

    private IBinder mBinder = new ShakeServiceBinder();

    private OnShakedListener mShakedListener;

    private TelephonyManager mTelephonyManager;

    private MediaPlayer mMediaPlayer;

    private ShakeAlarmClock mAlarm;

    private static final long[] VIBRATE_PATTERN = new long[] { 1000, 800 };

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

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(new TelphoneListener(), PhoneStateListener.LISTEN_CALL_STATE);

        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unregisterSensorListener();
        stopAlarm();
        Utils.removeAlarmIcon(this);
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        registerSensorListener();
        mAlarm = (ShakeAlarmClock)intent.getSerializableExtra(Constants.ALARM_RAW_DATA);
        playAlarm();
        return mBinder;
    }

    private void playAlarm() {
        Uri alertUri = Uri.parse(mAlarm.getRingUri());
        try {
            mMediaPlayer.setDataSource(this, alertUri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (null != mVibrator && mAlarm.isVibrate()) {
            mVibrator.vibrate(VIBRATE_PATTERN, 0);
        }
    }

    private void stopAlarm() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        if (null != mVibrator && mAlarm.isVibrate()) {
            mVibrator.cancel();
        }
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

    private class TelphoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(LOG_TAG, "offhook");
                    mVibrator.cancel();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(LOG_TAG, "idle");
                    mMediaPlayer.start();
                    mVibrator.vibrate(VIBRATE_PATTERN, 0);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(LOG_TAG, "ringing");
                    mMediaPlayer.pause();
                    mVibrator.cancel();
                    break;
                default:
                    break;
            }
        }
    }

    int count = 0;
    int n = 14;
    int level = 9;
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
