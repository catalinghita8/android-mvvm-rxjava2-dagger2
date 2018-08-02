package com.inspiringteam.xchange.di.quakes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.inspiringteam.xchange.di.ViewModelKey;
import com.inspiringteam.xchange.di.scopes.ActivityScoped;
import com.inspiringteam.xchange.ui.quakes.QuakesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class QuakesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(QuakesViewModel.class)
    abstract ViewModel bindQuakesViewModel(QuakesViewModel ratesViewModel);

    @Binds
    @ActivityScoped
    abstract ViewModelProvider.Factory bindViewModelFactory(QuakesViewModelFactory factory);
}
