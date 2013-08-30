package com.alarmclock.shake.app.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    Context context;

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "shakealarm.db";

    public final String TABLE_CLOCK = "clocks";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // Called only once, first time the DB is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CLOCK + " ("
                + ClockData.ClocksColumns._ID + " INTEGER PRIMARY KEY, "
                + ClockData.ClocksColumns.TIME + " TEXT, "
                + ClockData.ClocksColumns.NAME + " TEXT, "
                + ClockData.ClocksColumns.OPEN + " INTEGER, "
                + ClockData.ClocksColumns.VIBRATE + " INTEGER, "
                + ClockData.ClocksColumns.REPEAT + " INTEGER, "
                + ClockData.ClocksColumns.DAYS + " TEXT, "
                + ClockData.ClocksColumns.RING_NAME + " TEXT, "
                + ClockData.ClocksColumns.RING_URI + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drops the old tables
        db.execSQL("drop table if exists " + TABLE_CLOCK);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}