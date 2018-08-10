package com.inspiringteam.xchange.data.source;

import android.content.Context;

import com.google.common.collect.Lists;
import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.util.ConnectivityUtils.OnlineChecker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the implementation of the repository
 */
public class QuakesRepositoryTest {
    private long testTime = System.currentTimeMillis();
    private long staleTime = 6 * 60 * 1000;

    private List<Quake> QUAKES_RECENT = Lists.newArrayList(
            new Quake(4.5, "Place1", testTime, testTime),
            new Quake(3.3, "Place2", testTime - 1, testTime));
    private List<Quake> QUAKES_STALE = Lists.newArrayList(
            new Quake(4.5, "Place1", testTime, testTime - staleTime),
            new Quake(3.3, "Place2", testTime - 1, testTime));

    private QuakesRepository mQuakesRepository;

    private TestSubscriber<List<Quake>> mQuakesTestSubscriber;

    @Mock
    private QuakesDataSource mQuakesRemoteDataSource;

    @Mock
    private QuakesDataSource mQuakesLocalDataSource;

    @Mock
    private OnlineChecker mOnlineChecker;

    @Before
    public void setupQuakesRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mQuakesRepository = new QuakesRepository
                (mQuakesRemoteDataSource, mQuakesLocalDataSource, mOnlineChecker);

        mQuakesTestSubscriber = new TestSubscriber<>();
    }

    /**
     * Offline Test scenario states:
     * As the disk has up-to-date items, upon querying the repository with no internet connection,
     * the Local Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getQuakesOffline_requestsQuakesFromLocalDataSource() {

        // the local data source has up-to-date data available
        new ArrangeBuilder()
                .withQuakesAvailable(mQuakesLocalDataSource, QUAKES_RECENT);

        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(false);

        // When quakes are requested from the quakes repository
        mQuakesRepository.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber);

        // Then quakes are loaded from the local data source
        verify(mQuakesLocalDataSource).getQuakes();
        mQuakesTestSubscriber.assertValue(QUAKES_RECENT);
    }

    /**
     * Online Test scenario states:
     * As the disk has up-to-date items, upon querying the repository with active internet connection,
     * the Local Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getQuakesOnline_requestsQuakesFromLocalDataSource_upToDateLocal() {

        // the local data source has up-to-date data available
        new ArrangeBuilder()
                .withQuakesAvailable(mQuakesLocalDataSource, QUAKES_RECENT);

        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(true);

        // When quakes are requested from the quakes repository
        mQuakesRepository.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber);

        // Then quakes are loaded from the local data source
        verify(mQuakesLocalDataSource).getQuakes();
        mQuakesTestSubscriber.assertValue(QUAKES_RECENT);
    }

    /**
     * Online Test scenario states:
     * As the disk has stale items, upon querying the repository with active internet connection,
     * the Remote Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getQuakesOnline_requestsQuakesFromRemoteDataSource_staleLocal() {

        // the remote data source has fresh data available
        new ArrangeBuilder()
                .withQuakesAvailable(mQuakesRemoteDataSource, QUAKES_RECENT);

        // the local data source has stale data available
        new ArrangeBuilder()
                .withQuakesAvailable(mQuakesLocalDataSource, QUAKES_STALE);

        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(true);

        // When quakes are requested from the quakes repository
        mQuakesRepository.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber);

        // Both sources should be queried, yet the local source has stale items
        // which triggers the call to the remote source
        verify(mQuakesLocalDataSource).getQuakes();
        verify(mQuakesLocalDataSource).getQuakes();

        mQuakesTestSubscriber.assertValue(QUAKES_RECENT);
    }

    /**
     * Online Test scenario states:
     * As the disk has no items, upon querying the repository with active internet connection,
     * the Remote Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getQuakesOnline_requestsQuakesFromRemoteDataSource_emptyLocal() {

        // the remote data source has fresh data available
        new ArrangeBuilder()
                .withQuakesAvailable(mQuakesRemoteDataSource, QUAKES_RECENT);

        // the local data source has stale data available
        new ArrangeBuilder()
                .withQuakesNotAvailable(mQuakesLocalDataSource);

        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(true);

        // When quakes are requested from the quakes repository
        mQuakesRepository.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber);

        // Both sources should be queried, yet the local source has no items
        // which triggers the call to the remote source
        verify(mQuakesLocalDataSource).getQuakes();
        verify(mQuakesLocalDataSource).getQuakes();

        mQuakesTestSubscriber.assertValue(QUAKES_RECENT);
    }

    /**
     * Test scenario states:
     * Upon get command , both Local Data Source should retrieve the item locally
     */
    @Test
    public void getQuakeFromLocal(){
        Quake tempQuake = new Quake("id", "location");

        new ArrangeBuilder().withQuakeById(mQuakesLocalDataSource, tempQuake);

        mQuakesRepository.getQuake(tempQuake.getId()).toFlowable().subscribe();

        // upon get command, check if only local data source is being called
        verify(mQuakesLocalDataSource).getQuake(tempQuake.getId());
        verify(mQuakesRemoteDataSource, never()).getQuake(tempQuake.getId());
    }


    /**
     * Test scenario states:
     * Upon save command , both Local Data Source and Remote Data Source should save
     *  the corresponding items taken as parameter
     */
    @Test
    public void saveQuakes(){
        mQuakesRepository.saveQuakes(QUAKES_RECENT);

        // upon save command, check if both data sources are being called
        verify(mQuakesLocalDataSource).saveQuakes(QUAKES_RECENT);
        verify(mQuakesRemoteDataSource).saveQuakes(QUAKES_RECENT);
    }

    /**
     * Test scenario states:
     * Upon save command , both Local Data Source and Remote Data Source should save
     *  the corresponding item taken as parameter
     */
    @Test
    public void saveQuake(){
        mQuakesRepository.saveQuake(QUAKES_RECENT.get(0));

        // upon save command, check if both data sources are being called
        verify(mQuakesLocalDataSource).saveQuake(QUAKES_RECENT.get(0));
        verify(mQuakesRemoteDataSource).saveQuake(QUAKES_RECENT.get(0));
    }

    /**
     * Test scenario states:
     * Upon delete command , both Local Data Source and Remote Data Source should delete
     *  all items
     */
    @Test
    public void deleteQuakes(){
        mQuakesRepository.deleteAllQuakes();

        // upon save command, check if both data sources are being called
        verify(mQuakesLocalDataSource).deleteAllQuakes();
        verify(mQuakesRemoteDataSource).deleteAllQuakes();
    }

    /**
     * Test scenario states:
     * Upon delete command , both Local Data Source and Remote Data Source should delete
     *  the corresponding item
     */
    @Test
    public void deleteQuake(){
        mQuakesRepository.deleteQuake("id");

        // upon save command, check if both data sources are being called
        verify(mQuakesLocalDataSource).deleteQuake("id");
        verify(mQuakesRemoteDataSource).deleteQuake("id");
    }


    class ArrangeBuilder {

        ArrangeBuilder withQuakesNotAvailable(QuakesDataSource dataSource) {
            when(dataSource.getQuakes()).thenReturn(Single.just(Collections.emptyList()));
            return this;
        }

        ArrangeBuilder withQuakesAvailable(QuakesDataSource dataSource, List<Quake> quakes) {
            // don't allow the data sources to complete.
            when(dataSource.getQuakes()).thenReturn(Single.just(quakes));
            return this;
        }

        ArrangeBuilder withQuakeById(QuakesDataSource dataSource,Quake quake) {
            // don't allow the data sources to complete.
            when(dataSource.getQuake(quake.getId())).thenReturn(Single.just(quake));
            return this;
        }
    }
}
