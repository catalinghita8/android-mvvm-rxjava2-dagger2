package com.inspiringteam.xchange.di;

import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.ui.quakes.QuakesNavigator;
import com.inspiringteam.xchange.util.providers.BaseNavigator;
import com.inspiringteam.xchange.util.providers.Navigator;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.inspiringteam.xchange.util.schedulers.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {
    @Provides
    @AppScoped
    QuakesNavigator provideQuakesNavigator(BaseNavigator navigationProvider) {
        return new QuakesNavigator(navigationProvider);
    }


    @Provides
    @AppScoped
    BaseNavigator provideBaseNavigator() {
        return new Navigator();
    }

    @AppScoped
    @Provides
    BaseSchedulerProvider provideSchedulerProvider(){
        return SchedulerProvider.getInstance();
    }
}
