package com.inspiringteam.xchange.data.source;

import android.content.Context;

import com.google.common.collect.Lists;
import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.util.DisplayUtils.SortUtils;
import com.inspiringteam.xchange.util.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the implementation of the repository
 */
public class QuakesRepositoryTest {
    private long testTime = System.currentTimeMillis();
    private List<Quake> QUAKES = Lists.newArrayList(
            new Quake(4.5, "Place1", testTime, testTime),
            new Quake(3.3, "Place2", testTime - 1, testTime));

    private QuakesRepository mQuakesRepository;

    private TestSubscriber<List<Quake>> mQuakesTestSubscriber;

    @Mock
    private QuakesDataSource mQuakesRemoteDataSource;

    @Mock
    private QuakesDataSource mQuakesLocalDataSource;

    @Mock
    private Context mContext;

    private TestSubscriber mTestSubscriber = new TestSubscriber();

    @Before
    public void setupQuakesRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mQuakesRepository = new QuakesRepository
                (mQuakesRemoteDataSource, mQuakesLocalDataSource);

        mQuakesTestSubscriber = new TestSubscriber<>();
    }

    /**
     * Test scenario states:
     * As the disk has up-to-date items, upon querying the repository,
     * the Local Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getQuakes_requestsQuakesFromLocalDataSource() {
        long testTime = System.currentTimeMillis();
        List<Quake> QUAKES = Lists.newArrayList(
                new Quake(4.5, "Place1", testTime, testTime),
                new Quake(3.3, "Place2", testTime - 1, testTime));

        // the local data source has up-to-date data available
        new ArrangeBuilder()
                .withQuakesAvailable(mQuakesLocalDataSource, QUAKES);


        // When quakes are requested from the quakes repository
        mQuakesRepository.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber);

        // Then quakes are loaded from the local data source
        verify(mQuakesLocalDataSource).getQuakes();
        mQuakesTestSubscriber.assertValue(QUAKES);
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
    }
}
