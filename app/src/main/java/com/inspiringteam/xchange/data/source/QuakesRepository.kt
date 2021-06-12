package com.inspiringteam.xchange.data.source

import com.google.common.base.Preconditions
import com.inspiringteam.xchange.data.models.Quake
import com.inspiringteam.xchange.data.source.scopes.Local
import com.inspiringteam.xchange.data.source.scopes.Remote
import com.inspiringteam.xchange.di.scopes.AppScoped
import com.inspiringteam.xchange.util.ConnectivityUtils.OnlineChecker
import com.inspiringteam.xchange.util.DisplayUtils.SortUtils
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

/**
 * Consists of a functional set of methods that allow ViewModels to access appropriate data flows
 */
@AppScoped
class QuakesRepository
/**
 * Dagger allows us to have a single instance of the repository throughout the app
 *
 * @param quakesRemoteDataSource the backend data source (Remote Source)
 * @param quakesLocalDataSource  the device storage data source (Local Source)
 */ @Inject constructor(
    @param:Remote private val quakesRemoteDataSource: QuakesDataSource,
    @param:Local private val quakesLocalDataSource: QuakesDataSource,
    private val onlineChecker: OnlineChecker
) : QuakesDataSource {
    /**
     * The retrieval logic sets the Local Source as the primary source
     * In case of an active internet connection and the absence of Local database
     * or if it contains stale data, the Remote Source is queried and the Local one is refreshed
     */
    override fun getQuakes(): Single<List<Quake>> {
        return quakesLocalDataSource.getQuakes()
            .flatMap { data: List<Quake> ->
                if (onlineChecker.isOnline() && (data.isEmpty() || isStale(data)))
                    return@flatMap getFreshQuakes()
                Single.just(SortUtils.sortByNewest(data))
            }
    }

    override fun getQuake(quakeId: String): Single<Quake> {
        Preconditions.checkNotNull(quakeId)
        return quakesLocalDataSource.getQuake(quakeId)
    }

    override fun saveQuakes(quakes: List<Quake>) {
        Preconditions.checkNotNull(quakes)
        quakesLocalDataSource.saveQuakes(quakes)
        quakesRemoteDataSource.saveQuakes(quakes)
    }

    override fun saveQuake(quake: Quake) {
        Preconditions.checkNotNull(quake)
        quakesLocalDataSource.saveQuake(quake)
        quakesRemoteDataSource.saveQuake(quake)
    }

    override fun deleteAllQuakes() {
        quakesLocalDataSource.deleteAllQuakes()
        quakesRemoteDataSource.deleteAllQuakes()
    }

    override fun deleteQuake(quakeId: String) {
        quakesLocalDataSource.deleteQuake(quakeId)
        quakesRemoteDataSource.deleteQuake(quakeId)
    }

    /**
     * Helper methods, should be encapsulated
     */
    private fun isStale(data: List<Quake>): Boolean {
        // It's enough for 1 item to be stale
        return !data[0].isUpToDate
    }

    /**
     * Contains data refreshing logic
     * Both sources are emptied, then new items are retrieved from querying the Remote Source
     * and finally, sources are replenished
     */
    private fun getFreshQuakes(): Single<List<Quake>> {
        deleteAllQuakes()
        return quakesRemoteDataSource
            .getQuakes()
            .doOnSuccess { quakes: List<Quake> ->
                saveQuakes(quakes)
            }
    }
}