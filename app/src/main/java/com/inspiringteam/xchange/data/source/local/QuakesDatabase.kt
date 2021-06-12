package com.inspiringteam.xchange.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inspiringteam.xchange.data.models.Quake

/**
 * The Room Database that contains the Quakes table.
 */
@Database(entities = [Quake::class], version = 2, exportSchema = false)
abstract class QuakesDatabase : RoomDatabase() {
    abstract fun quakesDao(): QuakesDao
}