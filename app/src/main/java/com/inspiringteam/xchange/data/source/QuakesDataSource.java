package com.inspiringteam.xchange.data.source;

import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.models.Quake;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface QuakesDataSource {
    @NonNull
    Single<List<Quake>> getQuakes();

    @NonNull
    Single<Quake> getQuake(@NonNull String quakeId);

    void saveQuakes(@NonNull List<Quake> quakes);

    void saveQuake(@NonNull Quake quake);

    void deleteAllQuakes();

    void deleteQuake(@NonNull String quakeId);
}
