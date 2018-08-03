package com.inspiringteam.xchange.data.source.remote;

import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.models.QuakesResponse;
import com.inspiringteam.xchange.data.source.QuakesDataSource;
import com.inspiringteam.xchange.di.scopes.AppScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


@AppScoped
public class QuakesRemoteDataSource implements QuakesDataSource {
    @NonNull
    private QuakesApiService mApiService;

    @Inject
    public QuakesRemoteDataSource(@NonNull QuakesApiService apiService) {
        mApiService = apiService;
    }

    @NonNull
    @Override
    public Observable<List<Quake>> getQuakes() {
        return   mApiService.getQuakes()
                .flatMap(response -> Observable.fromIterable(response.quakeWrapperList).toList().toObservable())
                .flatMap(wrappersResponse -> Observable.fromIterable(wrappersResponse)
                        .map(wrapper -> wrapper.quake).toList().toObservable());

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
