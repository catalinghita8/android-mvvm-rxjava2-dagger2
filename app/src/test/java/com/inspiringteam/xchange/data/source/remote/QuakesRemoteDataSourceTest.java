package com.inspiringteam.xchange.data.source.remote;

import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.models.QuakeWrapper;
import com.inspiringteam.xchange.data.models.QuakesResponse;
import com.inspiringteam.xchange.data.source.QuakesDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test
 * SUT - {@link QuakesRemoteDataSource}
 */
public class QuakesRemoteDataSourceTest {
    @Mock
    QuakesApiService mQuakeService;

    private QuakesRemoteDataSource mRemoteDataSource;

    @Before
    public void setup() throws Exception {
        // init mocks
        MockitoAnnotations.initMocks(this);

        // get reference to the class in test
        mRemoteDataSource = new QuakesRemoteDataSource(mQuakeService);

    }


    @Test
    public void testPreConditions() {
        assertNotNull(mRemoteDataSource);
    }

    /**
     * Test scenario states:
     * Remote Source should get the correct results in success scenario
     */
    @Test
    public void testRemoteApiResponse_success() throws Exception {
        TestSubscriber<List<Quake>> testSubscriber = new TestSubscriber<>();

        List<Quake> listQuakes = new ArrayList<>();

        // set up mock response
        QuakesResponse mockQuakeResponse = new QuakesResponse();
        Quake tempQuake = new Quake("id", "location");
        listQuakes.add(tempQuake);

        QuakeWrapper quakeWrapper = new QuakeWrapper(tempQuake);
        List<QuakeWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(quakeWrapper);

        mockQuakeResponse.setquakeWrapperList(wrapperList);

        // prepare fake response
        when(mQuakeService.getQuakes())
                .thenReturn(Single.just(mockQuakeResponse));

        // trigger response
        mRemoteDataSource.getQuakes().toFlowable().subscribe(testSubscriber);

        List<Quake> result = testSubscriber.values().get(0);

        testSubscriber.assertValue(listQuakes);
    }

    /**
     * Test scenario states:
     * Remote Source should get the correct results in failure scenario
     */
    @Test
    public void testRemoteApiResponse_failure() throws Exception {
        TestSubscriber<List<Quake>> testSubscriber = new TestSubscriber<>();

        // prepare fake exception
        Throwable exception = new IOException();

        // prepare fake response
        when(mQuakeService.getQuakes()).
                thenReturn(Single.<QuakesResponse>error(exception));

        // assume the repository calls the remote DataSource
        mRemoteDataSource.getQuakes().toFlowable().subscribe(testSubscriber);

        testSubscriber.assertError(IOException.class);
    }
}
