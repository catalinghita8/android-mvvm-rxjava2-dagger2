package com.inspiringteam.xchange.di;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.inspiringteam.xchange.di.scopes.ActivityScoped;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.ui.quakes.QuakesActivity;
import com.inspiringteam.xchange.ui.quakes.QuakesNavigator;
import com.inspiringteam.xchange.util.ChromeTabsUtils.ChromeTabsWrapper;
import com.inspiringteam.xchange.util.ConnectivityUtils.DefaultOnlineChecker;
import com.inspiringteam.xchange.util.ConnectivityUtils.OnlineChecker;
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
    ConnectivityManager provideConnectivityManager(Application context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @AppScoped
    @Provides
    ChromeTabsWrapper providesChromeTabsWrapper(Application context) {
        return new ChromeTabsWrapper(context);
    }

    @Provides
    @AppScoped
    OnlineChecker onlineChecker(ConnectivityManager cm) {
        return new DefaultOnlineChecker(cm);
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
