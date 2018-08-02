package com.inspiringteam.xchange.ui.quakes;

import com.inspiringteam.xchange.di.quakes.QuakesViewModelModule;
import com.inspiringteam.xchange.di.scopes.ActivityScoped;
import com.inspiringteam.xchange.di.scopes.FragmentScoped;
import com.inspiringteam.xchange.util.providers.BaseNavigator;
import com.inspiringteam.xchange.util.providers.BaseResourceProvider;
import com.inspiringteam.xchange.util.providers.Navigator;
import com.inspiringteam.xchange.util.providers.ResourceProvider;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.inspiringteam.xchange.util.schedulers.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;


@Module (includes = {QuakesModule.QuakesAbstractModule.class, QuakesViewModelModule.class})
public class QuakesModule {

    @ActivityScoped
    @Provides
    BaseSchedulerProvider provideSchedulerProvider(){
        return SchedulerProvider.getInstance();
    }

    @ActivityScoped
    @Provides
    BaseResourceProvider provideResourceProvider(QuakesActivity context) {
        return new ResourceProvider(context);
    }


    @Provides
    @ActivityScoped
    QuakesNavigator provideQuakesNavigator(BaseNavigator navigationProvider) {
        return new QuakesNavigator(navigationProvider);
    }


    @Provides
    @ActivityScoped
    BaseNavigator provideBaseNavigator(QuakesActivity activity) {
        return new Navigator();
    }

    @Module
    public interface QuakesAbstractModule {
        @FragmentScoped
        @ContributesAndroidInjector
        QuakesFragment ratesFragment();
    }
}
