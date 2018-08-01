package com.inspiringteam.xchange.ui.rates;

import com.inspiringteam.xchange.di.rates.RatesViewModelModule;
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


@Module (includes = {RatesModule.RatesAbstractModule.class, RatesViewModelModule.class})
public class RatesModule {

    @ActivityScoped
    @Provides
    BaseSchedulerProvider provideSchedulerProvider(){
        return SchedulerProvider.getInstance();
    }

    @ActivityScoped
    @Provides
    BaseResourceProvider provideResourceProvider(RatesActivity context) {
        return new ResourceProvider(context);
    }


    @Provides
    @ActivityScoped
    RatesNavigator provideRatesNavigator(BaseNavigator navigationProvider) {
        return new RatesNavigator(navigationProvider);
    }


    @Provides
    @ActivityScoped
    BaseNavigator provideBaseNavigator(RatesActivity activity) {
        return new Navigator();
    }

    @Module
    public interface RatesAbstractModule {
        @FragmentScoped
        @ContributesAndroidInjector
        RatesFragment ratesFragment();
    }
}
