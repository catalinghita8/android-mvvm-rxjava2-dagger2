package com.inspiringteam.xchange.data.source.local

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.inspiringteam.xchange.data.models.Quake
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class QuakesDaoTest constructor() {
    private var mDatabase: QuakesDatabase? = null
    private var mQuakeTestSubscriber: TestSubscriber<Quake>? = null
    private var mQuakesTestSubscriber: TestSubscriber<MutableList<Quake>>? = null
    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            QuakesDatabase::class.java
        ).build()
        mQuakeTestSubscriber = TestSubscriber()
        mQuakesTestSubscriber = TestSubscriber<MutableList<Quake>>()
    }

    @After
    fun closeDb() {
        mDatabase!!.close()
    }

    /**
     * Test scenario states:
     * Upon insertion of a quake, the correct item is retrieved
     */
    @Test
    fun insertQuakeAndGetById() {
        // insert quake
        mDatabase!!.quakesDao().insertQuake(QUAKES.get(0))

        // getting the Quake  by id from the database
        mDatabase!!.quakesDao()
            .getQuakeById(QUAKES.get(0).id).toFlowable().subscribe(mQuakeTestSubscriber)

        // the loaded data contains the expected values
        mQuakeTestSubscriber.assertValue(QUAKES.get(0))
    }

    /**
     * Test scenario states:
     * Upon insertion of quakes, the correct list is retrieved
     */
    @Test
    fun insertQuakesAndGet() {
        // insert quakes
        mDatabase!!.quakesDao().insertQuake(QUAKES.get(0))
        mDatabase!!.quakesDao().insertQuake(QUAKES.get(1))
        mDatabase!!.quakesDao().insertQuake(QUAKES.get(2))


        // getting quakes from the database
        mDatabase!!.quakesDao()
            .getQuakes().toFlowable().subscribe(mQuakesTestSubscriber)

        // the loaded data contains the expected values
        mQuakesTestSubscriber.assertValue(QUAKES)
    }

    /**
     * Test scenario states:
     * Upon insertion of a conflictual quake, the latter one should be retrieved
     */
    @Test
    fun insertQuakeAndReplaceOnConflict() {
        val conflictualQuake: Quake = Quake("id1", "locationN")

        // insert  initial quake
        mDatabase!!.quakesDao().insertQuake(QUAKES.get(0))

        // insert  conflictual quake
        mDatabase!!.quakesDao().insertQuake(conflictualQuake)


        // getting quake from the database
        mDatabase!!.quakesDao()
            .getQuakeById("id1").toFlowable().subscribe(mQuakeTestSubscriber)

        // the loaded data contains the expected values
        mQuakeTestSubscriber!!.assertValue(conflictualQuake)
    }

    /**
     * Test scenario states:
     * Upon insertion of 1 quake  and deletion of all records, we should retrieve an empty list
     * fom the database
     */
    @Test
    fun deleteQuakeAndGetQuakes_Scenario() {
        // given a quake (item) inserted
        mDatabase!!.quakesDao().insertQuake(QUAKES.get(0))

        // deleting all data
        mDatabase!!.quakesDao().deleteQuakes()

        // getting the data
        mDatabase!!.quakesDao().getQuakes().toFlowable().subscribe(mQuakesTestSubscriber)

        // the list should be empty
        mQuakesTestSubscriber!!.assertValue(ArrayList<Quake>())
    }

    companion object {
        private val QUAKES: MutableList<Quake> = Arrays.asList(
            Quake("id1", "location1"),
            Quake("id2", "location2"),
            Quake("id3", "location3")
        )
    }
}