package com.inspiringteam.xchange.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.scopes.Local;
import com.inspiringteam.xchange.data.source.scopes.Remote;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.ConnectivityUtils.OnlineChecker;
import com.inspiringteam.xchange.util.DisplayUtils.SortUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Consists of a functional set of methods that allow ViewModels to access appropriate data flows
 */

@AppScoped
public class QuakesRepository implements QuakesDataSource {

    private final QuakesDataSource mQuakesRemoteDataSource;

    private final QuakesDataSource mQuakesLocalDataSource;

    private final OnlineChecker mOnlineChecker;

    /**
     * Dagger allows us to have a single instance of the repository throughout the app
     *
     * @param quakesRemoteDataSource the backend data source (Remote Source)
     * @param quakesLocalDataSource  the device storage data source (Local Source)
     */
    @Inject
    public QuakesRepository(@Remote QuakesDataSource quakesRemoteDataSource,
                            @Local QuakesDataSource quakesLocalDataSource,
                            OnlineChecker onlineChecker) {
        mQuakesRemoteDataSource = quakesRemoteDataSource;
        mQuakesLocalDataSource = quakesLocalDataSource;
        mOnlineChecker = onlineChecker;
    }

    /**
     * The retrieval logic sets the Local Source as the primary source
     * In case of an active internet connection and the absence of Local database
     * or if it contains stale data, the Remote Source is queried and the Local one is refreshed
     */
    // TODO ADD INTERNET ACCESS CHECKER AND LOGIC
    @NonNull
    @Override
    public Single<List<Quake>> getQuakes() {
        return mQuakesLocalDataSource.getQuakes()
                .flatMap(data -> {
                    if (mOnlineChecker.isOnline() && (data.isEmpty() || isStale(data))) {
                        return getFreshQuakes();
                    }
                    return Single.just(SortUtils.sortByNewest(data));
                });
    }

    @NonNull
    @Override
    public Single<Quake> getQuake(@NonNull String quakeId) {
        checkNotNull(quakeId);
        return mQuakesLocalDataSource.getQuake(quakeId);
    }

    @Override
    public void saveQuakes(@NonNull List<Quake> quakes) {
        checkNotNull(quakes);
        mQuakesLocalDataSource.saveQuakes(quakes);
        mQuakesRemoteDataSource.saveQuakes(quakes);
    }

    @Override
    public void saveQuake(@NonNull Quake quake) {
        checkNotNull(quake);
        mQuakesLocalDataSource.saveQuake(quake);
        mQuakesRemoteDataSource.saveQuake(quake);
    }

    @Override
    public void deleteAllQuakes() {
        mQuakesLocalDataSource.deleteAllQuakes();
        mQuakesRemoteDataSource.deleteAllQuakes();
    }

    @Override
    public void deleteQuake(@NonNull String quakeId) {
        mQuakesLocalDataSource.deleteQuake(quakeId);
        mQuakesRemoteDataSource.deleteQuake(quakeId);
    }

    /**
     * Helper methods, should be encapsulated
     */

    private boolean isStale(List<Quake> data) {
        // it is enough for 1 item to be stale
        return !data.get(0).isUpToDate();
    }

    /**
     * Contains data refreshing logic
     * Both sources are emptied, then new items are retrieved from querying the Remote Source
     * and finally, sources are replenished
     */
    private Single<List<Quake>> getFreshQuakes() {
        deleteAllQuakes();

        return mQuakesRemoteDataSource.getQuakes().
                doOnSuccess(this::saveQuakes);
    }
}
