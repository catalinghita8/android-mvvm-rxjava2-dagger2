package com.inspiringteam.xchange.data.source.local

import androidx.room.*
import com.inspiringteam.xchange.data.models.Quake
import io.reactivex.Single
import java.util.*

/**
 * Room Dao interface
 */
@Dao
interface QuakesDao {
    @Query("SELECT * FROM Quakes ")
    fun getQuakes(): Single<MutableList<Quake>>

    /**
     * Retrieve a quake by id.
     *
     * @param quakeId the Quake id.
     * @return the quake with quakeId
     */
    @Query("SELECT * FROM Quakes WHERE id = :quakeId")
    fun getQuakeById(quakeId: String): Single<Quake>

    /**
     * Insert Quake in the database. If the Quake already exists, ignore the action.
     *
     * @param Quake to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuake(Quake: Quake)

    /**
     * Delete a Quake by id.
     *
     * @return the number of Quakes deleted. This should always be 1.
     */
    @Query("DELETE FROM Quakes WHERE id = :QuakeId")
    fun deleteQuakeById(QuakeId: String): Int

    /**
     * Delete all Quake (items).
     */
    @Query("DELETE FROM Quakes")
    fun deleteQuakes()

    @Update
    fun updateQuake(Quake: Quake): Int
}