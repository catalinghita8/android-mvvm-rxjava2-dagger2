package com.inspiringteam.xchange.data.source.local;

import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.QuakesDataSource;
import com.inspiringteam.xchange.di.scopes.AppScoped;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
@AppScoped
public class QuakesLocalDataSource implements QuakesDataSource{
    private final QuakesDao mQuakesDao;

    @Inject
    public QuakesLocalDataSource(@NonNull QuakesDao quakesDao) {
        checkNotNull(quakesDao, "quakesDao cannot be null");
        mQuakesDao = quakesDao;
    }

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

    @NonNull
    @Override
    public Completable saveQuakes(@NonNull List<Quake> quakes) {
        checkNotNull(quakes);
        return Completable.fromAction(() -> {
            for(Quake quake : quakes) mQuakesDao.insertQuake(quake);
        });
    }

    @NonNull
    @Override
    public Completable saveQuake(@NonNull Quake quake) {
        checkNotNull(quake);
        return Completable.fromAction(() -> {
            mQuakesDao.insertQuake(quake);
        });
    }

    @NonNull
    @Override
    public Completable refreshQuakes() {
        return null;
    }

    @Override
    public void deleteAllQuakes() {

    }

    @Override
    public void deleteQuake(@NonNull String quakeId) {

    }
}
