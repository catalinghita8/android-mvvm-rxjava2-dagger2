package com.inspiringteam.xchange.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.scopes.Local;
import com.inspiringteam.xchange.data.source.scopes.Remote;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.inspiringteam.xchange.util.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;


/**
 * TODO
 */
@AppScoped
public class QuakesRepository  implements QuakesDataSource{

    private final QuakesDataSource mQuakesRemoteDataSource;

    private final QuakesDataSource mQuakesLocalDataSource;

    @NonNull
    private final BaseSchedulerProvider mBaseSchedulerProvider;


    @Inject
    public QuakesRepository(@Remote QuakesDataSource quakesRemoteDataSource,
                            @Local QuakesDataSource quakesLocalDataSource) {
        mQuakesRemoteDataSource = quakesRemoteDataSource;
        mQuakesLocalDataSource = quakesLocalDataSource;
        mBaseSchedulerProvider = SchedulerProvider.getInstance();
    }

    /**
     * TODO
     */
    @NonNull
    @Override
    public Observable<List<Quake>> getQuakes(){
//        List<Quake> quakes = new ArrayList<>();
//        quakes.add(new Quake(2.4, "Some quake 1"));
//        quakes.add(new Quake(1.9, "Some Quake 2"));
//
//        return Observable.just(quakes);
        return mQuakesRemoteDataSource.getQuakes();
    }

    /**
     * TODO
     */
    @NonNull
    @Override
    public Observable<Quake> getQuake(@NonNull String quakeId) {
        return null;
    }

    /**
     * TODO
     */
    @NonNull
    @Override
    public Completable saveQuakes(@NonNull List<Quake> quakes) {
        return null;
    }

    /**
     * TODO
     */
    @NonNull
    @Override
    public Completable saveQuake(@NonNull Quake quake) {
        return null;
    }

    /**
     * TODO
     */
    @NonNull
    @Override
    public Completable refreshQuakes() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void deleteAllQuakes() {

    }

    /**
     * TODO
     */
    @Override
    public void deleteQuake(@NonNull String quakeId) {

    }
}
