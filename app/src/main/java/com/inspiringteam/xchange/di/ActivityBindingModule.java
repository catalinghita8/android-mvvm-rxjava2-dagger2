package com.inspiringteam.xchange.di;

import com.inspiringteam.xchange.ui.rates.RatesActivity;
import com.inspiringteam.xchange.di.scopes.ActivityScoped;
import com.inspiringteam.xchange.ui.rates.RatesModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of
 * whichever module ActivityBindingModule is on (AppComponent, here).
 * we never need to tell AppComponent that it is going to have all or any of these subcomponents
 * nor do we need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and
 * be aware of a scope annotation @ActivityScoped
 * In this case, when Dagger.Android annotation processor runs it will create 1 subcomponent for us
 */
@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = {RatesModule.class})
    abstract RatesActivity ratesActivity();
}
