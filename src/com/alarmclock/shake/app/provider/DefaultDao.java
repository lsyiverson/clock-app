
package com.alarmclock.shake.app.provider;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;

import com.alarmclock.shake.app.model.ShakeAlarmClock;


public class DefaultDao {

    private final ReadWriteLock mLock = new ReentrantReadWriteLock();

    private final Lock mReadLock = mLock.readLock(); // 读锁

    private final Lock mWriteLock = mLock.writeLock(); // 写锁

    private final ContentResolver mContentResolver;

    private DBHelper mDBHelper;

    public DefaultDao(Context context) {
        mContentResolver = context.getContentResolver();
        mDBHelper = new DBHelper(context);
    }

    public void insertClock(final ShakeAlarmClock shakeAlarmClock, final boolean notifyChange) {
        mWriteLock.lock();
        try {
            ContentValues values = new ContentValues();
            values = getContentClockValues(shakeAlarmClock);
            mContentResolver.insert(ClockData.Clocks.CONTENT_URI, values);
            if (notifyChange) {
                mContentResolver.notifyChange(ClockData.Clocks.CONTENT_URI, null);
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    public void updateClock(ShakeAlarmClock shakeAlarmClock, final boolean notifyChange) {
        mWriteLock.lock();
        try {
            ContentValues values = new ContentValues();
            values = getContentClockValues(shakeAlarmClock);
            String selection = ClockData.ClocksColumns._ID + "=" + "\'" + shakeAlarmClock.getId() + "\'";
            int row = mContentResolver.update(ClockData.Clocks.CONTENT_URI, values, selection, null);
            if (notifyChange) {
                mContentResolver.notifyChange(ClockData.Clocks.CONTENT_URI, null);
            }
        } finally {
            mWriteLock.unlock();
        }
    }

    public ArrayList<ShakeAlarmClock> getClocks(){
        mReadLock.lock();
        try {
            ArrayList<ShakeAlarmClock> shakeAlarmClocks = new ArrayList<ShakeAlarmClock>();
            String sortOrder = ClockData.ClocksColumns._ID;
            Cursor cursor = mContentResolver.query(ClockData.Clocks.CONTENT_URI, null, null, null, sortOrder);
            if (cursor != null && cursor.getCount() > 0) {
                ShakeAlarmClock shakeAlarmClock = null;
                while (cursor.moveToNext()) {
                    shakeAlarmClock = getClock(cursor);
                    if (shakeAlarmClock != null) {
                        shakeAlarmClocks.add(shakeAlarmClock);
                    }
                }
            }
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
            return shakeAlarmClocks;
        } finally {
            mReadLock.unlock();
        }
    }

    public CursorLoader queryClocks(Context context) {
        mReadLock.lock();
        try {
            CursorLoader loader = null;
            String sortOrder = ClockData.ClocksColumns._ID;
            loader = new CursorLoader(context, ClockData.Clocks.CONTENT_URI, null, null, null,
                    sortOrder);
            return loader;
        } finally {
            mReadLock.unlock();
        }
    }

    public int delClock(Context context, String id, final boolean notifyChange) {
        mWriteLock.lock();
        int row = 0;
        String selection = ClockData.ClocksColumns._ID + "=" + "\'" + id + "\'";
        try {
            row = mContentResolver.delete(ClockData.Clocks.CONTENT_URI, selection, null);
            if (notifyChange) {
                mContentResolver.notifyChange(ClockData.Clocks.CONTENT_URI, null);
            }
            return row;
        } finally {
            mWriteLock.unlock();
        }
    }

    public int queryCount(String userId) {
        mReadLock.lock();
        try {
            Cursor cursor = null;
            int count = 0;
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            try {
                cursor = db.rawQuery("select count(*)from " + mDBHelper.TABLE_CLOCK, null);
                cursor.moveToFirst();
                count = cursor.getInt(0);
            } finally {
                if (cursor != null) {
                    try {
                        cursor.close();
                        db.close();
                    } catch (Exception e) {
                    }
                }
            }
            cursor.close();
            return count;
        } finally {
            mReadLock.unlock();
        }
    }

    public ContentValues getContentClockValues(ShakeAlarmClock bean) {
        ContentValues values = null;
        if (bean != null) {
            values = new ContentValues();
            values.put(ClockData.Clocks.TIME, bean.getTime());
            values.put(ClockData.Clocks.NAME, bean.getName());
            values.put(ClockData.Clocks.OPEN, bean.getOpen());
            values.put(ClockData.Clocks.REPEAT, bean.getRepeat());
            values.put(ClockData.Clocks.VIBRATE, bean.getVibrate());
            values.put(ClockData.Clocks.DAYS, bean.getDayString());
            values.put(ClockData.Clocks.RING_NAME, bean.getRingName());
            values.put(ClockData.Clocks.RING_URI, bean.getRingUri());
        }
        return values;
    }

    public ShakeAlarmClock getClock(Cursor cursor) {
        ShakeAlarmClock shakeAlarmClock = null;
        if (cursor != null) {

            int id = cursor.getInt(cursor.getColumnIndex(ClockData.ClocksColumns._ID));

            String time = cursor.getString(cursor.getColumnIndex(ClockData.ClocksColumns.TIME));

            String name = cursor.getString(cursor.getColumnIndex(ClockData.ClocksColumns.NAME));

            int open = cursor.getInt(cursor.getColumnIndex(ClockData.ClocksColumns.OPEN));

            int repeat = cursor.getInt(cursor.getColumnIndex(ClockData.ClocksColumns.REPEAT));

            int vibrate = cursor.getInt(cursor.getColumnIndex(ClockData.ClocksColumns.VIBRATE));

            String days = cursor.getString(cursor.getColumnIndex(ClockData.ClocksColumns.DAYS));

            String ringName = cursor.getString(cursor
                    .getColumnIndex(ClockData.ClocksColumns.RING_NAME));

            String ringUri = cursor.getString(cursor
                    .getColumnIndex(ClockData.ClocksColumns.RING_URI));

            shakeAlarmClock = new ShakeAlarmClock();
            shakeAlarmClock.setId(id);
            shakeAlarmClock.setTime(time);
            shakeAlarmClock.setName(name);
            shakeAlarmClock.setOpen(open);
            shakeAlarmClock.setRepeat(repeat);
            shakeAlarmClock.setVibrate(vibrate);
            shakeAlarmClock.setDayString(days);
            shakeAlarmClock.setRingName(ringName);
            shakeAlarmClock.setRingUri(ringUri);
        }
        return shakeAlarmClock;
    }
}
