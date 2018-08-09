package com.inspiringteam.xchange.data.source.remote;

import com.inspiringteam.xchange.data.models.QuakesResponse;


import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface QuakesApiService {
    @GET("1.0_day.geojson")
    Single<QuakesResponse> getQuakes();
}
