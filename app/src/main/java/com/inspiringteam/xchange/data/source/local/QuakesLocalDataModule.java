package com.inspiringteam.xchange.data.source.local;


import android.app.Application;

import com.inspiringteam.xchange.di.scopes.ActivityScoped;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.inspiringteam.xchange.util.schedulers.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class QuakesLocalDataModule {
    @AppScoped
    @Provides
    BaseSchedulerProvider provideSchedulerProvider(){
        return SchedulerProvider.getInstance();
    }

    @AppScoped
    @Provides
    QuakesDbHelper provideQuakesDbHelper(Application context){
        return new QuakesDbHelper(context.getApplicationContext());
    }
}
