package com.inspiringteam.xchange.data.source.local;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.Constants;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.inspiringteam.xchange.util.schedulers.SchedulerProvider;

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
