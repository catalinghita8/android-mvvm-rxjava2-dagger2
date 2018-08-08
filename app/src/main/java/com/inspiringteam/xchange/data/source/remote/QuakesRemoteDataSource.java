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
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Remote Data Source implementation
 */
@AppScoped
public class QuakesRemoteDataSource implements QuakesDataSource {
    @NonNull
    private QuakesApiService mApiService;

    @Inject
    public QuakesRemoteDataSource(@NonNull QuakesApiService apiService) {
        mApiService = apiService;
    }

    /**
     * Fresh items are retrieved from Remote API
     */
    @NonNull
    @Override
    public Single<List<Quake>> getQuakes() {
        return mApiService.getQuakes()
                .flatMap(response -> Observable.fromIterable(response.quakeWrapperList).toList())
                .flatMap(wrappersResponse -> Observable.fromIterable(wrappersResponse)
                        .map(wrapper -> {
                            wrapper.quake.setTimeStamp(System.currentTimeMillis());
                            return wrapper.quake;
                        }).toList());

    }

    /**
     * These methods should be implemented when required
     * (e.g. when a cloud service is integrated)
     */
    @NonNull
    @Override
    public Single<Quake> getQuake(@NonNull String quakeId) {
        return null;
    }

    @NonNull
    @Override
    public void saveQuakes(@NonNull List<Quake> quakes) {
    }

    @NonNull
    @Override
    public void saveQuake(@NonNull Quake quake) {
    }

    @Override
    public void deleteAllQuakes() {

    }

    @Override
    public void deleteQuake(@NonNull String quakeId) {

    }
}
