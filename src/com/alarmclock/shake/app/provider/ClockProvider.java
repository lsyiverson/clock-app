package com.alarmclock.shake.app.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ClockProvider extends ContentProvider {

    private static final String TABLE_CLOCKS = "clocks";

    private static final int MATCH_CLOCKS = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ClockData.AUTHORITY, "clocks", MATCH_CLOCKS);
    }

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case MATCH_CLOCKS:
                tableName = TABLE_CLOCKS;
                break;
            default:
                throw new IllegalArgumentException("query() - Unknown uri: " + uri);
        }
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public synchronized String getType(Uri uri) {
        String mimeType = "";
        switch (sUriMatcher.match(uri)) {
            case MATCH_CLOCKS:
                mimeType = ClockData.Clocks.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new IllegalArgumentException("getType() - Unknown uri: " + uri);
        }
        return mimeType;
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String tableName = null;
        boolean inserted = false;
        long rowId = -1;
        switch (sUriMatcher.match(uri)) {
            case MATCH_CLOCKS:
                tableName = TABLE_CLOCKS;
                break;
            default:
                throw new UnsupportedOperationException("Can't insert into uri: " + uri);
        }
        if (!inserted) {
            rowId = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        return Uri.withAppendedPath(uri, String.valueOf(rowId));
    }

    @Override
    public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
        int affectedRows = 0;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case MATCH_CLOCKS:
                affectedRows = db.delete(TABLE_CLOCKS, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Can't delete uri: " + uri);
        }
        return affectedRows;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case MATCH_CLOCKS:
                tableName = TABLE_CLOCKS;
                break;
            default:
                throw new UnsupportedOperationException("Can't update uri: " + uri);
        }
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        return db.updateWithOnConflict(tableName, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_NONE);
    }
}
