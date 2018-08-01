package com.inspiringteam.xchange.ui.rates;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.source.RatesRepository;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

/**
 * ViewModel for the list of rates.
 */

public final class RatesViewModel extends ViewModel{
    private static final String TAG = RatesViewModel.class.getSimpleName();

    @NonNull
    private final RatesRepository mRepository;

    @NonNull
    private final RatesNavigator mNavigator;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @Inject
    public RatesViewModel(@NonNull RatesRepository ratesRepository,
                          @NonNull RatesNavigator navigationProvider,
                          @NonNull BaseSchedulerProvider schedulerProvider) {
        mNavigator = navigationProvider;
        mRepository = ratesRepository;
        mSchedulerProvider = schedulerProvider;
    }
//    @Inject
//    public RatesViewModel() { }

}
