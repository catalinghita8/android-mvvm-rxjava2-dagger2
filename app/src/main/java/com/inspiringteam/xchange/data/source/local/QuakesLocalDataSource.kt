package com.inspiringteam.xchange.data.source.local

import com.google.common.base.Preconditions
import com.inspiringteam.xchange.data.models.Quake
import com.inspiringteam.xchange.data.source.QuakesDataSource
import com.inspiringteam.xchange.di.scopes.AppScoped
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

/**
 * Concrete implementation of the Local Data Source
 */
@AppScoped
class QuakesLocalDataSource @Inject constructor(
    quakesDao: QuakesDao,
    schedulerProvider: BaseSchedulerProvider
) : QuakesDataSource {

    private val mQuakesDao: QuakesDao
    private val mSchedulerProvider: BaseSchedulerProvider

    init {
        Preconditions.checkNotNull(schedulerProvider, "scheduleProvider cannot be null")
        Preconditions.checkNotNull(quakesDao, "quakesDao cannot be null")
        mQuakesDao = quakesDao
        mSchedulerProvider = schedulerProvider
    }

    /**
     * Items are retrieved from disk
     */
    override fun getQuakes(): Single<List<Quake>> {
        return mQuakesDao.getQuakes().map { it.toList() }
    }

    override fun getQuake(quakeId: String): Single<Quake> {
        return mQuakesDao.getQuakeById(quakeId)
    }

    override fun saveQuakes(quakes: List<Quake>) {
        Preconditions.checkNotNull(quakes)
        for (quake in quakes) saveQuake(quake)
    }

    override fun saveQuake(quake: Quake) {
        Preconditions.checkNotNull(quake)
        Completable.fromRunnable { mQuakesDao.insertQuake(quake) }
            .subscribeOn(mSchedulerProvider.io()).subscribe()
    }

    override fun deleteAllQuakes() {
        Completable.fromRunnable { mQuakesDao.deleteQuakes() }
            .subscribeOn(mSchedulerProvider.io()).subscribe()
    }

    override fun deleteQuake(quakeId: String) {
        Completable.fromRunnable { mQuakesDao.deleteQuakeById(quakeId) }
            .subscribeOn(mSchedulerProvider.io()).subscribe()
    }

}