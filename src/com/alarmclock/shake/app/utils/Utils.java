package com.alarmclock.shake.app.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {

    private static Typeface mRobotoLight;

    public static Typeface getRobotoLightTypeface(Context context) {
        if (mRobotoLight == null) {
            mRobotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        }
        return mRobotoLight;
    }
}
