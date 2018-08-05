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
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;


@AppScoped
public class QuakesRepository implements QuakesDataSource {

    private final QuakesDataSource mQuakesRemoteDataSource;

    private final QuakesDataSource mQuakesLocalDataSource;

    @NonNull
    private final BaseSchedulerProvider mBaseSchedulerProvider;


    @Inject
    public QuakesRepository(@Remote QuakesDataSource quakesRemoteDataSource,
                            @Local QuakesDataSource quakesLocalDataSource,
                            BaseSchedulerProvider schedulerProvider) {
        mQuakesRemoteDataSource = quakesRemoteDataSource;
        mQuakesLocalDataSource = quakesLocalDataSource;
        mBaseSchedulerProvider = schedulerProvider;
    }

    @NonNull
    @Override
    // TODO fix - no data is being retrieved from local store
    public Single<List<Quake>> getQuakes() {
        return mQuakesLocalDataSource.getQuakes().flatMap(data -> {
            if(data.isEmpty() || isStale(data))
                // after that is retrieved remotely, refresh the cache - TODO rename save quakes to refresh
                return mQuakesRemoteDataSource.getQuakes()
                        .doOnSuccess(list -> mQuakesLocalDataSource.saveQuakes(list));

            return Single.just(data);
        });
    }

    private boolean isStale(List<Quake> data) {
        return !data.get(0).isUpToDate();
    }

    @NonNull
    @Override
    public Single<Quake> getQuake(@NonNull String quakeId) {
        checkNotNull(quakeId);
        return mQuakesLocalDataSource.getQuake(quakeId);
    }

    @NonNull
    @Override
    public Completable saveQuakes(@NonNull List<Quake> quakes) {
        checkNotNull(quakes);
        return mQuakesLocalDataSource.saveQuakes(quakes)
                .andThen(mQuakesRemoteDataSource.saveQuakes(quakes));
    }

    @NonNull
    @Override
    public Completable saveQuake(@NonNull Quake quake) {
        checkNotNull(quake);
        return mQuakesLocalDataSource.saveQuake(quake)
                .andThen(mQuakesRemoteDataSource.saveQuake(quake));
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
