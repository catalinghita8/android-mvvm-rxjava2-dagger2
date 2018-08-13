package com.inspiringteam.xchange.data.source.local;

import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.QuakesDataSource;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;


import java.util.List;


import javax.inject.Inject;


import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of the Local Data Source
 */
@AppScoped
public class QuakesLocalDataSource implements QuakesDataSource {
    private final QuakesDao mQuakesDao;

    private BaseSchedulerProvider mSchedulerProvider;

    @Inject
    public QuakesLocalDataSource(@NonNull QuakesDao quakesDao,
                                 @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(schedulerProvider, "scheduleProvider cannot be null");
        checkNotNull(quakesDao, "quakesDao cannot be null");

        mQuakesDao = quakesDao;
        mSchedulerProvider = schedulerProvider;
    }


    /**
     * Items are retrieved from disk
     */
    @NonNull
    @Override
    public Single<List<Quake>> getQuakes() {
        return mQuakesDao.getQuakes();
    }

    @NonNull
    @Override
    public Single<Quake> getQuake(@NonNull String quakeId) {
        return mQuakesDao.getQuakeById(quakeId);
    }

    @Override
    public void saveQuakes(@NonNull List<Quake> quakes) {
        checkNotNull(quakes);
        for (Quake quake : quakes)
            saveQuake(quake);
    }

    @Override
    public void saveQuake(@NonNull Quake quake) {
        checkNotNull(quake);
        Completable.fromRunnable(() -> mQuakesDao.insertQuake(quake))
                .subscribeOn(mSchedulerProvider.io()).subscribe();
    }

    @Override
    public void deleteAllQuakes() {
        Completable.fromRunnable(mQuakesDao::deleteQuakes)
                .subscribeOn(mSchedulerProvider.io()).subscribe();
    }

    @Override
    public void deleteQuake(@NonNull String quakeId) {
        Completable.fromRunnable(() -> mQuakesDao.deleteQuakeById(quakeId))
                .subscribeOn(mSchedulerProvider.io()).subscribe();
    }
}
