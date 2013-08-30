package com.alarmclock.shake.app.utils;

import android.content.Context;

public class DensityUtil {
    /**
     * Translate the dp(density-independent pixels) to px(pixels), according to
     * the resolution of device;
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * Translate the px(pixels) to dp(density-independent pixels), according to
     * the resolution of device;
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
