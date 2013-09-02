
package com.alarmclock.shake.app.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class Utils {

    private static Typeface mRobotoLight;

    private static final String ACTION_ALARM_CHANGED = "android.intent.action.ALARM_CHANGED";

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static Typeface getRobotoLightTypeface(Context context) {
        if (mRobotoLight == null) {
            mRobotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        }
        return mRobotoLight;
    }

    public static String getTitleByUri(Context ctx, Uri uri) {
        String uriString = "";
        try {
            ContentResolver cr = ctx.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null) {

                if (cursor.moveToNext()) {
                    uriString = cursor.getString(cursor.getColumnIndex("title"));
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uriString;
    }

    public static void printCursorContent(String logTag, Cursor cursor) {
        if (cursor == null) {
            Log.d(logTag, "Cursor is NULL!");
            return;
        }
        final int columnSpace = 2;
        ArrayList<Integer> columnWidth = new ArrayList<Integer>();
        for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
            String value = cursor.getColumnName(columnIndex);
            int maxWidth = value.length();
            if (cursor.moveToFirst()) {
                do {
                    try {
                        value = cursor.getString(columnIndex);
                    } catch (Exception e) {
                        value = "BLOB";
                        Log.d(logTag, "Get value from " + cursor.getColumnName(columnIndex)
                                + " failed. Caused by " + e.getLocalizedMessage());
                    }
                    if (!TextUtils.isEmpty(value) && value.length() > maxWidth) {
                        maxWidth = value.length();
                    }
                } while (cursor.moveToNext());
            }
            columnWidth.add(maxWidth + columnSpace);
        }
        ArrayList<ArrayList<String>> tableContent = new ArrayList<ArrayList<String>>();
        for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
            ArrayList<String> columnContent = new ArrayList<String>();
            String value = cursor.getColumnName(columnIndex);
            columnContent.add(appendColumnSpaces(value, columnWidth.get(columnIndex)));
            if (cursor.moveToFirst()) {
                do {
                    try {
                        value = cursor.getString(columnIndex);
                    } catch (Exception e) {
                        value = "BLOB";
                    }
                    columnContent.add(appendColumnSpaces(value, columnWidth.get(columnIndex)));
                } while (cursor.moveToNext());
            }
            tableContent.add(columnContent);
        }
        // Including the header
        int maxRowIndex = cursor.getCount() + 1;
        for (int rowIndex = 0; rowIndex < maxRowIndex; rowIndex++) {
            StringBuilder rowValues = new StringBuilder();
            for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
                ArrayList<String> columnValues = tableContent.get(columnIndex);
                rowValues.append(columnValues.get(rowIndex));
            }
            Log.d(logTag, rowValues.toString());
        }
        // set the cursor back the first item
        cursor.moveToFirst();
    }

    private static String appendColumnSpaces(String value, int columnWidth) {
        StringBuilder builder = new StringBuilder();
        int spaceCount;
        if (value == null) {
            builder.append("null");
            spaceCount = columnWidth - 4;
        } else {
            builder.append(value);
            spaceCount = columnWidth - value.length();
        }
        for (int i = 0; i < spaceCount; i++) {
            builder.append(" ");
        }
        return builder.toString();
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
