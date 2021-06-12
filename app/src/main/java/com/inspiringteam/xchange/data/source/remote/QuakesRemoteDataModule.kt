package com.inspiringteam.xchange.data.source.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.inspiringteam.xchange.di.scopes.AppScoped
import com.inspiringteam.xchange.util.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class QuakesRemoteDataModule {
    @AppScoped
    @Provides
    fun provideQuakesService(retrofit: Retrofit): QuakesApiService {
        return retrofit.create(QuakesApiService::class.java)
    }

    @Provides
    @AppScoped
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.QUAKES_API_BASE_URL)
            .build()
    }

    @Provides
    @AppScoped
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

}