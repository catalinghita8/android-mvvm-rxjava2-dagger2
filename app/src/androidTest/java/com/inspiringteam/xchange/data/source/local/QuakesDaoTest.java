package com.inspiringteam.xchange.data.source.local;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.inspiringteam.xchange.data.models.Quake;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.subscribers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class QuakesDaoTest {

    private QuakesDatabase mDatabase;

    private TestSubscriber<Quake> mQuakeTestSubscriber;
    private TestSubscriber<List<Quake>> mQuakesTestSubscriber;

    private static final List<Quake> QUAKES = Arrays.asList(
            new Quake("id1", "location1"),
            new Quake("id2", "location2"),
            new Quake("id3", "location3"));

    @Before
    public void initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                QuakesDatabase.class).build();

        mQuakeTestSubscriber = new TestSubscriber<>();
        mQuakesTestSubscriber = new TestSubscriber<>();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    /**
     * Test scenario states:
     * Upon insertion of a quake, the correct item is retrieved
     */
    @Test
    public void insertQuakeAndGetById() {
        // insert quake
        mDatabase.quakesDao().insertQuake(QUAKES.get(0));

        // getting the Quake  by id from the database
        mDatabase.quakesDao()
                .getQuakeById(QUAKES.get(0).getId()).toFlowable().subscribe(mQuakeTestSubscriber);

        // the loaded data contains the expected values
        mQuakeTestSubscriber.assertValue(QUAKES.get(0));
    }

    /**
     * Test scenario states:
     * Upon insertion of quakes, the correct list is retrieved
     */
    @Test
    public void insertQuakesAndGet() {
        // insert quakes
        mDatabase.quakesDao().insertQuake(QUAKES.get(0));
        mDatabase.quakesDao().insertQuake(QUAKES.get(1));
        mDatabase.quakesDao().insertQuake(QUAKES.get(2));


        // getting quakes from the database
        mDatabase.quakesDao()
                .getQuakes().toFlowable().subscribe(mQuakesTestSubscriber);

        // the loaded data contains the expected values
        mQuakesTestSubscriber.assertValue(QUAKES);
    }

    /**
     * Test scenario states:
     * Upon insertion of a conflictual quake, the latter one should be retrieved
     */
    @Test
    public void insertQuakeAndReplaceOnConflict() {
        Quake conflictualQuake = new Quake("id1", "locationN");

        // insert  initial quake
        mDatabase.quakesDao().insertQuake(QUAKES.get(0));

        // insert  conflictual quake
        mDatabase.quakesDao().insertQuake(conflictualQuake);


        // getting quake from the database
        mDatabase.quakesDao()
                .getQuakeById("id1").toFlowable().subscribe(mQuakeTestSubscriber);

        // the loaded data contains the expected values
        mQuakeTestSubscriber.assertValue(conflictualQuake);
    }

    /**
     * Test scenario states:
     * Upon insertion of 1 quake  and deletion of all records, we should retrieve an empty list
     * fom the database
     */
    @Test
    public void deleteQuakeAndGetQuakes_Scenario() {
        // given a quake (item) inserted
        mDatabase.quakesDao().insertQuake(QUAKES.get(0));

        // deleting all data
        mDatabase.quakesDao().deleteQuakes();

        // getting the data
        mDatabase.quakesDao().getQuakes().toFlowable().subscribe(mQuakesTestSubscriber);

        // the list should be empty
        mQuakesTestSubscriber.assertValue(new ArrayList<>());
    }
}
