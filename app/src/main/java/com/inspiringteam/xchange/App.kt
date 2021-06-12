package com.inspiringteam.xchange

import com.inspiringteam.xchange.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * We create a custom Application class that extends  [DaggerApplication].
 * We then override applicationInjector() which tells Dagger how to make our @AppScoped Component
 * We never have to call `component.inject(this)` as [DaggerApplication] will do that for us.
 */
class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}