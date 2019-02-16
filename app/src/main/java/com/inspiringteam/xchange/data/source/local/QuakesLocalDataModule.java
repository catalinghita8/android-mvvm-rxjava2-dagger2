package com.inspiringteam.xchange.data.source.local;


import android.app.Application;
import androidx.room.Room;

import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.Constants;

import dagger.Module;
import dagger.Provides;

@Module
public class QuakesLocalDataModule {
    @AppScoped
    @Provides
    QuakesDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                QuakesDatabase.class, Constants.QUAKES_ROOM_DB_STRING)
                .build();
    }

    @AppScoped
    @Provides
    QuakesDao provideQuakesDao(QuakesDatabase db) {
        return db.quakesDao();
    }
}
