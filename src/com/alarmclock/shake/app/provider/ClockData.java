package com.alarmclock.shake.app.provider;

import android.net.Uri;
import android.provider.BaseColumns;


public interface ClockData {

    String AUTHORITY = "com.alarmclock.shake.app.provider";

    interface ClocksColumns extends BaseColumns {
        String TIME = "time";
        String NAME= "name";
        String OPEN = "open";
        String REPEAT = "repeat";
        String VIBRATE = "vibrate";
        String DAYS = "days";
        String RING_NAME = "ring_name";
        String RING_URI = "ring_uri";
    }

    class Clocks implements ClocksColumns {
        /**
         * The content uri of this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://com.alarmclock.shake.app.provider/clocks");
        /**
         * The MIME type
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.alarmclock.shake.app.provider.clocks";
        /**
         * The MIME type
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.alarmclock.shake.app.provider.clocks";
    }
}


