package com.inspiringteam.xchange.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the quakes locally.
 */
public final class QuakesPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private QuakesPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class QuakeEntry implements BaseColumns {
        public static final String TABLE_NAME = "quake";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_MAGNITUDE = "magnitude";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_GRAVITY = "gravity";
    }
}