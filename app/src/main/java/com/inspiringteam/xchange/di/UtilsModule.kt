package com.inspiringteam.xchange.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.inspiringteam.xchange.di.scopes.AppScoped
import com.inspiringteam.xchange.util.chromeTabsUtils.ChromeTabsWrapper
import com.inspiringteam.xchange.util.ConnectivityUtils.DefaultOnlineChecker
import com.inspiringteam.xchange.util.ConnectivityUtils.OnlineChecker
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider
import com.inspiringteam.xchange.util.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {
    @Provides
    @AppScoped
    fun provideConnectivityManager(context: Application): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @AppScoped
    @Provides
    fun providesChromeTabsWrapper(app: Application): ChromeTabsWrapper {
        return ChromeTabsWrapper(app.applicationContext)
    }

    @Provides
    @AppScoped
    fun onlineChecker(cm: ConnectivityManager): OnlineChecker {
        return DefaultOnlineChecker(cm)
    }

    @AppScoped
    @Provides
    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider.instance
    }
}