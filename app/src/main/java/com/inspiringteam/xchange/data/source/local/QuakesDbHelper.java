package com.inspiringteam.xchange.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuakesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Quakes.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String REAL_TYPE = " REAL";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + QuakesPersistenceContract.QuakeEntry.TABLE_NAME + " (" +
                    QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + " PRIMARY KEY," +
                    QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_MAGNITUDE + REAL_TYPE + COMMA_SEP + // DOUBLE TYPE? store as REAL
                    QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP + // Long -store as int and retrieve cursor.getLong()
                    QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_GRAVITY + INTEGER_TYPE  +
                    " )";

    public QuakesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
