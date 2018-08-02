package com.inspiringteam.xchange.data.source;

import android.support.annotation.NonNull;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.di.scopes.AppScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;


@AppScoped
public class QuakesRepository  implements QuakesDataSource{
    @Inject
    public QuakesRepository() {
    }

    @NonNull
    @Override
    public Observable<List<Quake>> getQuakes(){
        List<Quake> quakes = new ArrayList<>();
        quakes.add(new Quake(2.4, "Some quake 1"));
        quakes.add(new Quake(1.9, "Some Quake 2"));

        return Observable.just(quakes);
    }
}
