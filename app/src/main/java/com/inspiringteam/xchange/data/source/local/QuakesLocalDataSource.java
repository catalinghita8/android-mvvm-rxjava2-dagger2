package com.inspiringteam.xchange.data.source.local;

import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.QuakesDataSource;
import com.inspiringteam.xchange.di.scopes.AppScoped;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;


@AppScoped
public class QuakesLocalDataSource implements QuakesDataSource{

    @Inject
    public QuakesLocalDataSource() {
    }

    @NonNull
    @Override
    public Observable<List<Quake>> getQuakes() {
        return null;
    }

    @NonNull
    @Override
    public Observable<Quake> getQuake(@NonNull String quakeId) {
        return null;
    }

    @NonNull
    @Override
    public Completable saveQuakes(@NonNull List<Quake> quakes) {
        return null;
    }

    @NonNull
    @Override
    public Completable saveQuake(@NonNull Quake quake) {
        return null;
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
