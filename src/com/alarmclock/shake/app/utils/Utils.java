package com.alarmclock.shake.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

public class Utils {

    private static Typeface mRobotoLight;

    private static final String ACTION_ALARM_CHANGED = "android.intent.action.ALARM_CHANGED";

    public static Typeface getRobotoLightTypeface(Context context) {
        if (mRobotoLight == null) {
            mRobotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        }
        return mRobotoLight;
    }

    public static void addAlarmIcon(Context context) {
        Intent alarmChanged = new Intent(ACTION_ALARM_CHANGED);
        alarmChanged.putExtra("alarmSet", true);
        context.sendBroadcast(alarmChanged);
    }

    public static void removeAlarmIcon(Context context) {
        Intent alarmChanged = new Intent(ACTION_ALARM_CHANGED);
        alarmChanged.putExtra("alarmSet", false);
        context.sendBroadcast(alarmChanged);
    }
}
