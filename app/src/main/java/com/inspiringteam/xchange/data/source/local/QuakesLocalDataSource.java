package com.inspiringteam.xchange.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.QuakesDataSource;
import com.inspiringteam.xchange.di.scopes.AppScoped;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
@AppScoped
public class QuakesLocalDataSource implements QuakesDataSource{
    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private Function<Cursor, Quake> mQuakeMapperFunction;

    @Inject
    public QuakesLocalDataSource(@NonNull QuakesDbHelper dbHelper,
    @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(dbHelper, "dbHelper cannot be null");
        checkNotNull(schedulerProvider, "scheduleProvider cannot be null");

        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());
        // method reference
        mQuakeMapperFunction = this::getQuake;
    }

    @NonNull
    private Quake getQuake(@NonNull Cursor c) {
        String id = c.getString(c.getColumnIndexOrThrow(QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_ENTRY_ID));
        Double magnitude = c.getDouble(c.getColumnIndexOrThrow(QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_MAGNITUDE));
        String location =
                c.getString(c.getColumnIndexOrThrow(QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_LOCATION));
        Long timestamp =
                c.getLong(c.getColumnIndexOrThrow(QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_TIMESTAMP));
        String url =
                c.getString(c.getColumnIndexOrThrow(QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_URL));
        int gravity =
                c.getInt(c.getColumnIndexOrThrow(QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_URL));

        return new Quake(id, magnitude, timestamp, location, url, gravity);
    }
    
    /** TODO - TEST IMPLEMENTATION
     * @return an Observable that emits the list of Quakes in the database, every time the Quakes
     * table is modified
     */
    @NonNull
    @Override
    public Observable<List<Quake>> getQuakes() {
        String[] projection = {
                QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_ENTRY_ID,
                QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_MAGNITUDE,
                QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_LOCATION,
                QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_URL,
                QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_TIMESTAMP,
                QuakesPersistenceContract.QuakeEntry.COLUMN_NAME_GRAVITY
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), QuakesPersistenceContract.QuakeEntry.TABLE_NAME);

        return mDatabaseHelper.createQuery(QuakesPersistenceContract.QuakeEntry.TABLE_NAME, sql)
                .mapToList(mQuakeMapperFunction);
    }

    @NonNull
    @Override
    public Observable<Quake> getQuake(@NonNull String quakeId) {
        return null;
    }

    @NonNull
    @Override
    public Completable saveQuakes(@NonNull List<Quake> quakes) {
        return null;
    }

    @NonNull
    @Override
    public Completable saveQuake(@NonNull Quake quake) {
        return null;
    }

    @NonNull
    @Override
    public Completable refreshQuakes() {
        return null;
    }

    @Override
    public void deleteAllQuakes() {

    }

    @Override
    public void deleteQuake(@NonNull String quakeId) {

    }
}
