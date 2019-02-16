package com.inspiringteam.xchange.data.source.local;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;
import com.inspiringteam.xchange.util.schedulers.ImmediateSchedulerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

import io.reactivex.subscribers.TestSubscriber;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

/**
 * Test
 * SUT - {@link QuakesLocalDataSource}
 */
@RunWith(AndroidJUnit4.class)
public class QuakesLocalDataSourceTest {

    private QuakesLocalDataSource mLocalDataSource;

    private QuakesDatabase mDatabase;

    private BaseSchedulerProvider mSchedulerProvider;

    private static final Quake QUAKE = new Quake("id", "location");

    @Before
    public void setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                QuakesDatabase.class)
                .build();
        QuakesDao quakesDao = mDatabase.quakesDao();

        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Make sure that we're not keeping a reference to the wrong instance.
        mLocalDataSource = new QuakesLocalDataSource(quakesDao, mSchedulerProvider);
    }

    @After
    public void cleanUp() {
        mDatabase.quakesDao().deleteQuakes();
        mDatabase.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon saving a quake
     */
    @Test
    public void saveQuake_retrievesQuake() {
        // When saved into the quakes repository
        mLocalDataSource.saveQuake(QUAKE);

        // Then the quake can be retrieved from the persistent repository
        TestSubscriber<Quake> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getQuake(QUAKE.getId()).toFlowable().subscribe(testSubscriber);

        testSubscriber.assertValue(QUAKE);
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon saving quakes
     */
    @Test
    public void getQuakes_retrieveSavedQuakes() {
        // Given 2 new quakes in the persistent repository
        final Quake newQuake1 = new Quake("id1", "location1");
        mLocalDataSource.saveQuake(newQuake1);

        final Quake newQuake2 = new Quake("id2", "location2");
        mLocalDataSource.saveQuake(newQuake2);

        // Then the quakes can be retrieved from the persistent repository
        TestSubscriber<List<Quake>> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getQuakes().toFlowable().subscribe(testSubscriber);

        List<Quake> result = testSubscriber.values().get(0);

        assertThat(result, hasItems(newQuake1, newQuake2));
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon having no data
     */
    @Test
    public void getQuake_whenQuakeNotSaved() {
        //Given that no Quake has been saved
        //When querying for a quake, no values are returned.
        TestSubscriber<Quake> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getQuake("some_id").toFlowable().subscribe(testSubscriber);

        testSubscriber.assertNoValues();
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon emptying source
     */
    @Test
    public void deleteAllQuakes_emptyListOfRetrievedQuakes() {
        // Given a new quake in the persistent repository
        mLocalDataSource.saveQuake(QUAKE);

        // When all quakes are deleted
        mLocalDataSource.deleteAllQuakes();

        // Then the retrieved quakes is an empty list
        TestSubscriber<List<Quake>> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getQuakes().toFlowable().subscribe(testSubscriber);

        List<Quake> result = testSubscriber.values().get(0);

        assertThat(result.size(), is(0));
    }
}
