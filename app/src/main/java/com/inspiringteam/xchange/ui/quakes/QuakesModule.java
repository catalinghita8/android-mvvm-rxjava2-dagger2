package com.inspiringteam.xchange.ui.quakes;

import com.inspiringteam.xchange.di.scopes.ActivityScoped;
import com.inspiringteam.xchange.di.scopes.FragmentScoped;
import com.inspiringteam.xchange.util.providers.BaseResourceProvider;
import com.inspiringteam.xchange.util.providers.ResourceProvider;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;


@Module (includes = {QuakesModule.QuakesAbstractModule.class})
public class QuakesModule {

    @ActivityScoped
    @Provides
    BaseResourceProvider provideResourceProvider(QuakesActivity context) {
        return new ResourceProvider(context);
    }

    @Module
    public interface QuakesAbstractModule {
        @FragmentScoped
        @ContributesAndroidInjector
        QuakesFragment ratesFragment();
    }
}
