package com.inspiringteam.xchange.data.source

import com.inspiringteam.xchange.data.models.Quake
import com.inspiringteam.xchange.util.ConnectivityUtils.OnlineChecker
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Unit tests for the implementation of the repository
 */
class QuakesRepositoryTest {
    private val testTime = System.currentTimeMillis()
    private val staleTime = (6 * 60 * 1000).toLong()

    private val QUAKES_RECENT: ArrayList<Quake> = arrayListOf<Quake>(
        Quake(4.5, "Place1", testTime, testTime),
        Quake(3.3, "Place2", testTime - 1, testTime)
    )
    private val QUAKES_STALE: ArrayList<Quake> = arrayListOf<Quake>(
        Quake(4.5, "Place1", testTime, testTime - staleTime),
        Quake(3.3, "Place2", testTime - 1, testTime)
    )

    private var mQuakesRepository: QuakesRepository? = null

    private var mQuakesTestSubscriber: TestSubscriber<MutableList<Quake>>? = null

    @Mock
    private val mQuakesRemoteDataSource: QuakesDataSource? = null

    @Mock
    private val mQuakesLocalDataSource: QuakesDataSource? = null

    @Mock
    private val mOnlineChecker: OnlineChecker? = null

    @Before
    fun setupQuakesRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        mQuakesRepository = QuakesRepository(
            mQuakesRemoteDataSource!!,
            mQuakesLocalDataSource!!, mOnlineChecker!!
        )
        mQuakesTestSubscriber = TestSubscriber<kotlin.collections.MutableList<Quake>>()
    }

    /**
     * Offline Test scenario states:
     * As the disk has up-to-date items, upon querying the repository with no internet connection,
     * the Local Data Source should be accessed and correct items should be retrieved
     */
    @Test
    fun getQuakesOffline_requestsQuakesFromLocalDataSource() {

        // the local data source has up-to-date data available
        ArrangeBuilder()
            .withQuakesAvailable(mQuakesLocalDataSource, QUAKES_RECENT)

        // establish a fake internet connection status
        Mockito.`when`(mOnlineChecker!!.isOnline()).thenReturn(false)

        // When quakes are requested from the quakes repository
        mQuakesRepository!!.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber)

        // Then quakes are loaded from the local data source
        Mockito.verify(mQuakesLocalDataSource).getQuakes()
        mQuakesTestSubscriber!!.assertValue(QUAKES_RECENT)
    }

    /**
     * Online Test scenario states:
     * As the disk has up-to-date items, upon querying the repository with active internet connection,
     * the Local Data Source should be accessed and correct items should be retrieved
     */
    @Test
    fun getQuakesOnline_requestsQuakesFromLocalDataSource_upToDateLocal() {

        // the local data source has up-to-date data available
        ArrangeBuilder()
            .withQuakesAvailable(mQuakesLocalDataSource, QUAKES_RECENT)

        // establish a fake internet connection status
        Mockito.`when`(mOnlineChecker!!.isOnline()).thenReturn(true)

        // When quakes are requested from the quakes repository
        mQuakesRepository!!.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber)

        // Then quakes are loaded from the local data source
        Mockito.verify(mQuakesLocalDataSource).getQuakes()
        mQuakesTestSubscriber!!.assertValue(QUAKES_RECENT)
    }

    /**
     * Online Test scenario states:
     * As the disk has stale items, upon querying the repository with active internet connection,
     * the Remote Data Source should be accessed and correct items should be retrieved
     */
    @Test
    fun getQuakesOnline_requestsQuakesFromRemoteDataSource_staleLocal() {

        // the remote data source has fresh data available
        ArrangeBuilder()
            .withQuakesAvailable(mQuakesRemoteDataSource, QUAKES_RECENT)

        // the local data source has stale data available
        ArrangeBuilder()
            .withQuakesAvailable(mQuakesLocalDataSource, QUAKES_STALE)

        // establish a fake internet connection status
        Mockito.`when`(mOnlineChecker!!.isOnline()).thenReturn(true)

        // When quakes are requested from the quakes repository
        mQuakesRepository!!.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber)

        // Both sources should be queried, yet the local source has stale items
        // which triggers the call to the remote source
        Mockito.verify(mQuakesLocalDataSource).getQuakes()
        Mockito.verify(mQuakesLocalDataSource).getQuakes()
        mQuakesTestSubscriber!!.assertValue(QUAKES_RECENT)
    }

    /**
     * Online Test scenario states:
     * As the disk has no items, upon querying the repository with active internet connection,
     * the Remote Data Source should be accessed and correct items should be retrieved
     */
    @Test
    fun getQuakesOnline_requestsQuakesFromRemoteDataSource_emptyLocal() {

        // the remote data source has fresh data available
        ArrangeBuilder()
            .withQuakesAvailable((mQuakesRemoteDataSource)!!, QUAKES_RECENT)

        // the local data source has stale data available
        ArrangeBuilder()
            .withQuakesNotAvailable((mQuakesLocalDataSource)!!)

        // establish a fake internet connection status
        Mockito.`when`(mOnlineChecker!!.isOnline()).thenReturn(true)

        // When quakes are requested from the quakes repository
        mQuakesRepository!!.getQuakes().toFlowable().subscribe(mQuakesTestSubscriber)

        // Both sources should be queried, yet the local source has no items
        // which triggers the call to the remote source
        Mockito.verify(mQuakesLocalDataSource).getQuakes()
        Mockito.verify(mQuakesLocalDataSource).getQuakes()
        mQuakesTestSubscriber!!.assertValue(QUAKES_RECENT)
    }

    /**
     * Test scenario states:
     * Upon get command , both Local Data Source should retrieve the item locally
     */
    @Test
    fun getQuakeFromLocal() {
        val tempQuake: Quake = Quake("id", "location")
        ArrangeBuilder().withQuakeById((mQuakesLocalDataSource)!!, tempQuake)
        mQuakesRepository!!.getQuake(tempQuake.id).toFlowable().subscribe()

        // upon get command, check if only local data source is being called
        Mockito.verify(mQuakesLocalDataSource).getQuake(tempQuake.id)
        Mockito.verify(mQuakesRemoteDataSource, Mockito.never()).getQuake(tempQuake.id)
    }


    /**
     * Test scenario states:
     * Upon save command , both Local Data Source and Remote Data Source should save
     * the corresponding items taken as parameter
     */
    @Test
    fun saveQuakes() {
        mQuakesRepository!!.saveQuakes(QUAKES_RECENT)

        // upon save command, check if both data sources are being called
        Mockito.verify(mQuakesLocalDataSource).saveQuakes(QUAKES_RECENT)
        Mockito.verify(mQuakesRemoteDataSource).saveQuakes(QUAKES_RECENT)
    }

    /**
     * Test scenario states:
     * Upon save command , both Local Data Source and Remote Data Source should save
     * the corresponding item taken as parameter
     */
    @Test
    fun saveQuake() {
        mQuakesRepository!!.saveQuake(QUAKES_RECENT.get(0))

        // upon save command, check if both data sources are being called
        Mockito.verify(mQuakesLocalDataSource).saveQuake(QUAKES_RECENT.get(0))
        Mockito.verify(mQuakesRemoteDataSource).saveQuake(QUAKES_RECENT.get(0))
    }

    /**
     * Test scenario states:
     * Upon delete command , both Local Data Source and Remote Data Source should delete
     * all items
     */
    @Test
    fun deleteQuakes() {
        mQuakesRepository!!.deleteAllQuakes()

        // upon save command, check if both data sources are being called
        Mockito.verify(mQuakesLocalDataSource).deleteAllQuakes()
        Mockito.verify(mQuakesRemoteDataSource).deleteAllQuakes()
    }

    /**
     * Test scenario states:
     * Upon delete command , both Local Data Source and Remote Data Source should delete
     * the corresponding item
     */
    @Test
    fun deleteQuake() {
        mQuakesRepository!!.deleteQuake("id")

        // upon save command, check if both data sources are being called
        Mockito.verify(mQuakesLocalDataSource).deleteQuake("id")
        Mockito.verify(mQuakesRemoteDataSource).deleteQuake("id")
    }


    internal class ArrangeBuilder constructor() {
        fun withQuakesNotAvailable(dataSource: QuakesDataSource): ArrangeBuilder {
            Mockito.`when`<Single<error.NonExistentClass>>(dataSource.getQuakes())
                .thenReturn(Single.just(emptyList()))
            return this
        }

        fun withQuakesAvailable(
            dataSource: QuakesDataSource,
            quakes: kotlin.collections.MutableList<Quake?>?
        ): ArrangeBuilder {
            // don't allow the data sources to complete.
            Mockito.`when`<Single<error.NonExistentClass>>(dataSource.getQuakes())
                .thenReturn(Single.just(quakes))
            return this
        }

        fun withQuakeById(dataSource: QuakesDataSource, quake: Quake): ArrangeBuilder {
            // don't allow the data sources to complete.
            Mockito.`when`(dataSource.getQuake(quake.id)).thenReturn(Single.just(quake))
            return this
        }
    }
}