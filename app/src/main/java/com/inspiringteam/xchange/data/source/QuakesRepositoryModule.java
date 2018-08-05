package com.inspiringteam.xchange.data.source;

import com.inspiringteam.xchange.data.source.local.QuakesDao;
import com.inspiringteam.xchange.data.source.local.QuakesLocalDataModule;
import com.inspiringteam.xchange.data.source.local.QuakesLocalDataSource;
import com.inspiringteam.xchange.data.source.remote.QuakesApiService;
import com.inspiringteam.xchange.data.source.remote.QuakesRemoteDataModule;
import com.inspiringteam.xchange.data.source.remote.QuakesRemoteDataSource;
import com.inspiringteam.xchange.data.source.scopes.Local;
import com.inspiringteam.xchange.data.source.scopes.Remote;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.inspiringteam.xchange.util.schedulers.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module (includes = {QuakesLocalDataModule.class, QuakesRemoteDataModule.class})
public class QuakesRepositoryModule {
    @Provides
    @Local
    @AppScoped
    QuakesDataSource provideQuakesLocalDataSource(QuakesDao quakesDao) {
        return new QuakesLocalDataSource(quakesDao);
    }

    @Provides
    @Remote
    @AppScoped
    QuakesDataSource provideQuakesRemoteDataSource(QuakesApiService apiService) {
        return new QuakesRemoteDataSource(apiService);
    }

    // might not be needed
    @AppScoped
    @Provides
    BaseSchedulerProvider provideSchedulerProvider(){
        return SchedulerProvider.getInstance();
    }
}
