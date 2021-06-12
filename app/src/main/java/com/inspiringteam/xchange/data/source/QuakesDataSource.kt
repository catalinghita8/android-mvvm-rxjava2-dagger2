package com.inspiringteam.xchange.data.source

import com.inspiringteam.xchange.data.models.Quake
import io.reactivex.Single

interface QuakesDataSource {
    fun getQuakes(): Single<List<Quake>>
    fun getQuake(quakeId: String): Single<Quake>
    fun saveQuakes(quakes: List<Quake>)
    fun saveQuake(quake: Quake)
    fun deleteAllQuakes()
    fun deleteQuake(quakeId: String)
}