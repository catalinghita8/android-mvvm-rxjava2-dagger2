package com.inspiringteam.xchange.data.source;

import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.models.Quake;

import java.util.List;

import rx.Observable;

public interface QuakesDataSource {
    @NonNull
    Observable<List<Quake>> getQuakes();
}
