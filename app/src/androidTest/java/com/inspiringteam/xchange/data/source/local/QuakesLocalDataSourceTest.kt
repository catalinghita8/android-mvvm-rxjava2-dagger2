package com.inspiringteam.xchange.data.source.local

import androidx.room.Room
import androidx.test.runner.AndroidJUnit4
import com.inspiringteam.xchange.data.models.Quake
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider
import com.inspiringteam.xchange.util.schedulers.ImmediateSchedulerProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test
 * SUT - [QuakesLocalDataSource]
 */
@RunWith(AndroidJUnit4::class)
class QuakesLocalDataSourceTest constructor() {
    private var mLocalDataSource: QuakesLocalDataSource? = null
    private var mDatabase: QuakesDatabase? = null
    private var mSchedulerProvider: BaseSchedulerProvider? = null
    @Before
    fun setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(
            androidx.test.InstrumentationRegistry.getContext(),
            QuakesDatabase::class.java
        )
            .build()
        val quakesDao: QuakesDao = mDatabase!!.quakesDao()
        mSchedulerProvider = ImmediateSchedulerProvider()

        // Make sure that we're not keeping a reference to the wrong instance.
        mLocalDataSource = QuakesLocalDataSource(quakesDao, mSchedulerProvider)
    }

    @org.junit.After
    fun cleanUp() {
        mDatabase!!.quakesDao().deleteQuakes()
        mDatabase!!.close()
    }

    @org.junit.Test
    fun testPreConditions() {
        junit.framework.Assert.assertNotNull(mLocalDataSource)
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon saving a quake
     */
    @org.junit.Test
    fun saveQuake_retrievesQuake() {
        // When saved into the quakes repository
        mLocalDataSource!!.saveQuake(QUAKE)

        // Then the quake can be retrieved from the persistent repository
        val testSubscriber: io.reactivex.subscribers.TestSubscriber<Quake> =
            io.reactivex.subscribers.TestSubscriber()
        mLocalDataSource!!.getQuake(QUAKE.id).toFlowable().subscribe(testSubscriber)
        testSubscriber.assertValue(QUAKE)
    }// Given 2 new quakes in the persistent repository

    // Then the quakes can be retrieved from the persistent repository
    /**
     * Test scenario states:
     * Local Source should get the correct result upon saving quakes
     */
    @get:Test
    val quakes_retrieveSavedQuakes: Unit
        get() {
            // Given 2 new quakes in the persistent repository
            val newQuake1: Quake = Quake("id1", "location1")
            mLocalDataSource!!.saveQuake(newQuake1)
            val newQuake2: Quake = Quake("id2", "location2")
            mLocalDataSource!!.saveQuake(newQuake2)

            // Then the quakes can be retrieved from the persistent repository
            val testSubscriber: io.reactivex.subscribers.TestSubscriber<kotlin.collections.MutableList<Quake>> =
                io.reactivex.subscribers.TestSubscriber<kotlin.collections.MutableList<Quake>>()
            mLocalDataSource!!.getQuakes().toFlowable().subscribe(testSubscriber)
            val result: kotlin.collections.MutableList<Quake> = testSubscriber.values().get(0)
            org.junit.Assert.assertThat<kotlin.collections.MutableList<Quake>>(
                result,
                org.hamcrest.core.IsCollectionContaining.hasItems<Quake?>(newQuake1, newQuake2)
            )
        }//Given that no Quake has been saved
    //When querying for a quake, no values are returned.
    /**
     * Test scenario states:
     * Local Source should get the correct result upon having no data
     */
    @get:Test
    val quake_whenQuakeNotSaved: Unit
        get() {
            //Given that no Quake has been saved
            //When querying for a quake, no values are returned.
            val testSubscriber: io.reactivex.subscribers.TestSubscriber<Quake> =
                io.reactivex.subscribers.TestSubscriber()
            mLocalDataSource!!.getQuake("some_id").toFlowable().subscribe(testSubscriber)
            testSubscriber.assertNoValues()
        }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon emptying source
     */
    @org.junit.Test
    fun deleteAllQuakes_emptyListOfRetrievedQuakes() {
        // Given a new quake in the persistent repository
        mLocalDataSource!!.saveQuake(QUAKE)

        // When all quakes are deleted
        mLocalDataSource!!.deleteAllQuakes()

        // Then the retrieved quakes is an empty list
        val testSubscriber: io.reactivex.subscribers.TestSubscriber<kotlin.collections.MutableList<Quake>> =
            io.reactivex.subscribers.TestSubscriber<kotlin.collections.MutableList<Quake>>()
        mLocalDataSource!!.getQuakes().toFlowable().subscribe(testSubscriber)
        val result: MutableList<Quake> = testSubscriber.values().get(0)
        org.junit.Assert.assertThat(result.size, org.hamcrest.core.Is.`is`(0))
    }

    companion object {
        private val QUAKE: Quake = Quake("id", "location")
    }
}