package com.inspiringteam.xchange.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.inspiringteam.xchange.di.quakes.QuakesViewModelFactory;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.ui.quakes.QuakesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(QuakesViewModel.class)
    abstract ViewModel bindQuakesViewModel(QuakesViewModel ratesViewModel);

    @Binds
    @AppScoped
    abstract ViewModelProvider.Factory bindViewModelFactory(QuakesViewModelFactory factory);
}
