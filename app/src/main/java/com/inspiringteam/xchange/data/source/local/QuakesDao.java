package com.inspiringteam.xchange.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.inspiringteam.xchange.data.models.Quake;

import java.util.List;

import io.reactivex.Single;

/**
 * Room Dao interface
 */
@Dao
public interface QuakesDao {

    @Query("SELECT * FROM Quakes ")
    Single<List<Quake>> getQuakes();


    /**
     * Retrieve a quake by id.
     *
     * @param quakeId the Quake id.
     * @return the quake with quakeId
     */
    @Query("SELECT * FROM Quakes WHERE id = :quakeId")
    Single<Quake> getQuakeById(String quakeId);
    


    /**
     * Insert Quake in the database. If the Quake already exists, ignore the action.
     *
     * @param Quake to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuake(Quake Quake);

    /**
     * Delete a Quake by id.
     *
     * @return the number of Quakes deleted. This should always be 1.
     */
    @Query("DELETE FROM Quakes WHERE id = :QuakeId")
    int deleteQuakeById(String QuakeId);


    /**
     * Delete all Quake (items).
     */
    @Query("DELETE FROM Quakes")
    void deleteQuakes();

    @Update
    int updateQuake(Quake Quake);
}

