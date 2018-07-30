package com.inspiringteam.xchange;

import com.inspiringteam.xchange.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * We create a custom Application class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @AppScoped Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class Application extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
