package com.inspiringteam.xchange.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.inspiringteam.xchange.data.models.Quake;

/**
 * The Room Database that contains the Quakes table.
 */

@Database(entities = {Quake.class}, version = 1, exportSchema = false)
public abstract class QuakesDatabase extends RoomDatabase {
    public abstract QuakesDao quakesDao();
}
