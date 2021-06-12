package com.inspiringteam.xchange.ui.quakes

import com.inspiringteam.xchange.di.scopes.ActivityScoped
import com.inspiringteam.xchange.di.scopes.FragmentScoped
import com.inspiringteam.xchange.ui.quakes.QuakesModule.QuakesAbstractModule
import com.inspiringteam.xchange.util.providers.BaseResourceProvider
import com.inspiringteam.xchange.util.providers.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [QuakesAbstractModule::class])
class QuakesModule {
    @ActivityScoped
    @Provides
    fun provideResourceProvider(context: QuakesActivity?): BaseResourceProvider {
        return ResourceProvider(context!!)
    }

    @Module
    interface QuakesAbstractModule {
        @FragmentScoped
        @ContributesAndroidInjector
        fun quakesFragment(): QuakesFragment?
    }
}