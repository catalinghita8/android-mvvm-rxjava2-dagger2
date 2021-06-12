package com.inspiringteam.xchange.data.source.remote

import com.inspiringteam.xchange.data.models.Quake
import com.inspiringteam.xchange.data.models.QuakeWrapper
import com.inspiringteam.xchange.data.models.QuakesResponse
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.IOException
import java.util.*

/**
 * Test
 * SUT - [QuakesRemoteDataSource]
 */
class QuakesRemoteDataSourceTest constructor() {
    @Mock
    var mQuakeService: QuakesApiService? = null
    private var mRemoteDataSource: QuakesRemoteDataSource? = null
    @Before
    @Throws(Exception::class)
    fun setup() {
        // init mocks
        MockitoAnnotations.initMocks(this)

        // get reference to the class in test
        mRemoteDataSource = QuakesRemoteDataSource((mQuakeService)!!)
    }

    @Test
    fun testPreConditions() {
        Assert.assertNotNull(mRemoteDataSource)
    }

    /**
     * Test scenario states:
     * Remote Source should get the correct results in success scenario
     */
    @Test
    @Throws(Exception::class)
    fun testRemoteApiResponse_success() {
        val testSubscriber: TestSubscriber<kotlin.collections.MutableList<Quake>> =
            TestSubscriber<kotlin.collections.MutableList<Quake>>()
        val listQuakes: kotlin.collections.MutableList<Quake> = ArrayList<Quake>()

        // set up mock response
        val mockQuakeResponse: QuakesResponse = QuakesResponse()
        val tempQuake: Quake = Quake("id", "location")
        listQuakes.add(tempQuake)
        val quakeWrapper: QuakeWrapper = QuakeWrapper(tempQuake)
        val wrapperList: kotlin.collections.MutableList<QuakeWrapper> = ArrayList<QuakeWrapper>()
        wrapperList.add(quakeWrapper)
        mockQuakeResponse.setquakeWrapperList(wrapperList)

        // prepare fake response
        Mockito.`when`(mQuakeService!!.quakes)
            .thenReturn(Single.just(mockQuakeResponse))

        // trigger response
        mRemoteDataSource!!.getQuakes().toFlowable().subscribe(testSubscriber)
        val result: kotlin.collections.MutableList<Quake> = testSubscriber.values().get(0)
        testSubscriber.assertValue(listQuakes)
    }

    /**
     * Test scenario states:
     * Remote Source should get the correct results in failure scenario
     */
    @Test
    @Throws(Exception::class)
    fun testRemoteApiResponse_failure() {
        val testSubscriber: TestSubscriber<kotlin.collections.MutableList<Quake>> =
            TestSubscriber<kotlin.collections.MutableList<Quake>>()

        // prepare fake exception
        val exception: Throwable = IOException()

        // prepare fake response
        Mockito.`when`(mQuakeService!!.quakes).thenReturn(Single.error(exception))

        // assume the repository calls the remote DataSource
        mRemoteDataSource!!.getQuakes().toFlowable().subscribe(testSubscriber)
        testSubscriber.assertError(IOException::class.java)
    }
}