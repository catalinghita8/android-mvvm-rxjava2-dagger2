package com.inspiringteam.xchange.di.rates;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.inspiringteam.xchange.di.ViewModelKey;
import com.inspiringteam.xchange.di.scopes.ActivityScoped;
import com.inspiringteam.xchange.ui.rates.RatesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RatesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel.class)
    abstract ViewModel bindRatesViewModel(RatesViewModel ratesViewModel);

    @Binds
    @ActivityScoped
    abstract ViewModelProvider.Factory bindViewModelFactory(RatesViewModelFactory factory);
}
