package com.inspiringteam.xchange.rates;

import com.inspiringteam.xchange.di.scopes.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class RatesModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract RatesFragment ratesFragment();
}
