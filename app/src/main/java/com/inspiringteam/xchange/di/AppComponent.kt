package com.inspiringteam.xchange.di

import com.inspiringteam.xchange.data.source.QuakesRepositoryModule
import com.inspiringteam.xchange.di.scopes.AppScoped
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import android.app.Application
import com.inspiringteam.xchange.App

/**
 * This is the root Dagger component.
 * [AndroidSupportInjectionModule]
 * is the module from Dagger.Android that helps with the generation
 * and location of subcomponents, which will be in our case, activities
 */
@AppScoped
@Component(
    modules = [
        QuakesRepositoryModule::class,
        ViewModelModule::class,
        UtilsModule::class,
        AppModule::class,
        ActivityBindingModule::class,
        AndroidSupportInjectionModule::class]
)
interface AppComponent : AndroidInjector<App> {

    // Application will just be provided into our app graph
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}