package com.inspiringteam.xchange.quakes;

import com.google.common.collect.Lists;
import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.QuakesRepository;
import com.inspiringteam.xchange.ui.quakes.QuakeItem;
import com.inspiringteam.xchange.ui.quakes.QuakesUiModel;
import com.inspiringteam.xchange.ui.quakes.QuakesViewModel;
import com.inspiringteam.xchange.util.chromeTabsUtils.ChromeTabsWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit test for {@link QuakesViewModel}
 */
public class QuakesViewModelTest {
    private static List<Quake> QUAKES;

    private QuakesViewModel mViewModel;

    private TestObserver<QuakesUiModel> mQuakesSubscriber;

    private TestObserver<Boolean> mProgressIndicatorSubscriber;

    private TestObserver<Integer> mSnackbarTextSubscriber;

    @Mock
    private QuakesRepository mQuakesRepository;

    @Mock
    private ChromeTabsWrapper mChromeTabsWrapper;

    @Before
    public void setupQuakesViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mViewModel = new QuakesViewModel(mQuakesRepository, mChromeTabsWrapper);


        QUAKES = Lists.newArrayList(new Quake("id1", "location1"),
                new Quake("id2", "location2"));

        mQuakesSubscriber = new TestObserver<>();
        mProgressIndicatorSubscriber = new TestObserver<>();
        mSnackbarTextSubscriber = new TestObserver<>();
    }

    @Test
    public void progressIndicator_emits_whenSubscribedToData() {
        // Given that the quake repository never emits
        when(mQuakesRepository.getQuakes()).thenReturn(Single.never());

        // Given that we are subscribed to the progress indicator
        mProgressIndicatorSubscriber = mViewModel.getLoadingIndicatorVisibility().test();

        // When subscribed to the quakes model
        mViewModel.getUiModel(false).subscribe();

        // The progress indicator emits initially false and then true
        mProgressIndicatorSubscriber.assertValues(false, true);
    }

    @Test
    public void snackbarText_emits_whenError_whenRetrievingData() {
        // Given an error when retrieving quakes
        when(mQuakesRepository.getQuakes()).thenReturn(Single.error(new RuntimeException()));

        // Given that we are subscribed to the snackbar text
        mViewModel.getSnackBarMessage().subscribe(mSnackbarTextSubscriber);

        // When subscribed to the quakes model
        mViewModel.getUiModel(false).subscribe(mQuakesSubscriber);

        // The snackbar emits an error message
        mSnackbarTextSubscriber.assertValue(R.string.loading_quakes_error);
    }

    @Test
    public void getQuakesModel_emits_whenQuakes() {
        // Given that we are subscribed to the emissions of the UI model
        withQuakesInRepositoryAndSubscribed(QUAKES);

        // The Quakes model containing the list of Quakes is emitted
        mQuakesSubscriber.assertValueCount(1);
        QuakesUiModel model = mQuakesSubscriber.values().get(0);
        assertQuakesModelWithQuakesVisible(model);
    }

    @Test
    public void forceUpdateQuakes_updatesQuakesRepository() {
        // Given that the quake repository never emits
        when(mQuakesRepository.getQuakes()).thenReturn(Single.never());

        // When calling force update
        mViewModel.getUiModel(true);

        // The quakes are refreshed in the repository
        verify(mQuakesRepository).deleteAllQuakes();
    }

    @Test
    public void QuakeItem_tapAction_opensQuakeDetails() {
        Quake tempQuake = new Quake("id", "location", "https://google.com");

        // Given a quake
        withQuakeInRepositoryAndSubscribed(tempQuake);
        // And list of quake items is emitted
        List<QuakeItem> items = mQuakesSubscriber.values().get(0).getQuakes();
        QuakeItem QuakeItem = items.get(0);

        // When triggering the click action
        QuakeItem.getOnClickAction().call();

        // Opening of the Quake details is called with the correct action
        verify(mChromeTabsWrapper).openCustomtab(tempQuake.getUrl());
    }

    private void withQuakeInRepositoryAndSubscribed(Quake Quake) {
        List<Quake> Quakes = new ArrayList<>();
        Quakes.add(Quake);
        withQuakesInRepositoryAndSubscribed(Quakes);
    }

    private void assertQuakesModelWithQuakesVisible(QuakesUiModel model) {
        assertTrue(model.isQuakesListVisible());
        assertQuakeItems(model.getQuakes());
        assertFalse(model.isNoQuakesViewVisible());
        assertNull(model.getNoQuakesModel());
    }

    private void withQuakesInRepositoryAndSubscribed(List<Quake> quakes) {
        // Given that the quake repository returns quakes
        when(mQuakesRepository.getQuakes()).thenReturn(Single.just(quakes));

        // Given that we are subscribed to the quakes
        mViewModel.getUiModel(false).subscribe(mQuakesSubscriber);
    }

    private void assertQuakeItems(List<QuakeItem> items) {
        // check if the QuakeItems are the expected ones
        assertEquals(items.size(), QUAKES.size());

        assertQuake(items.get(0), QUAKES.get(0));
        assertQuake(items.get(1), QUAKES.get(1));
    }

    private void assertQuake(QuakeItem QuakeItem, Quake Quake) {
        assertEquals(QuakeItem.getQuake(), Quake);
        assertNotNull(QuakeItem.getOnClickAction());
    }
}
