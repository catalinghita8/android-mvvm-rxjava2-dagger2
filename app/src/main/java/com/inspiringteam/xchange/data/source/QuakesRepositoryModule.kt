package com.inspiringteam.xchange.data.source

import com.inspiringteam.xchange.data.source.local.QuakesDao
import com.inspiringteam.xchange.data.source.local.QuakesLocalDataModule
import com.inspiringteam.xchange.data.source.local.QuakesLocalDataSource
import com.inspiringteam.xchange.data.source.remote.QuakesApiService
import com.inspiringteam.xchange.data.source.remote.QuakesRemoteDataModule
import com.inspiringteam.xchange.data.source.remote.QuakesRemoteDataSource
import com.inspiringteam.xchange.data.source.scopes.Local
import com.inspiringteam.xchange.data.source.scopes.Remote
import com.inspiringteam.xchange.di.scopes.AppScoped
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider
import dagger.Module
import dagger.Provides

@Module(includes = [QuakesLocalDataModule::class, QuakesRemoteDataModule::class])
class QuakesRepositoryModule {
    @Provides
    @Local
    @AppScoped
    fun provideQuakesLocalDataSource(
        quakesDao: QuakesDao?,
        schedulerProvider: BaseSchedulerProvider?
    ): QuakesDataSource {
        return QuakesLocalDataSource(quakesDao!!, schedulerProvider!!)
    }

    @Provides
    @Remote
    @AppScoped
    fun provideQuakesRemoteDataSource(apiService: QuakesApiService?): QuakesDataSource {
        return QuakesRemoteDataSource(apiService!!)
    }
}