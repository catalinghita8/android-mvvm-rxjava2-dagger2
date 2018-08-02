package com.inspiringteam.xchange.ui.quakes;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.data.models.Quake;
import com.inspiringteam.xchange.data.source.QuakesRepository;
import com.inspiringteam.xchange.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * ViewModel for the list of quake.
 */

public final class QuakesViewModel extends ViewModel{
    private static final String TAG = QuakesViewModel.class.getSimpleName();

    @NonNull
    private final QuakesRepository mRepository;

    @NonNull
    private final QuakesNavigator mNavigator;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    // using a BehaviourSubject because we are interested in the last object that was emitted before
    // subscribing. Like this we ensure that the loading indicator has the correct visibility.
    private final BehaviorSubject<Boolean> mLoadingIndicatorSubject;

    // using a PublishSubject because we are not interested in the last object that was emitted
    // before subscribing. Like this we avoid displaying the snackbar multiple times
    @NonNull
    private final PublishSubject<Integer> mSnackbarText;

    @Inject
    public QuakesViewModel(@NonNull QuakesRepository ratesRepository,
                          @NonNull QuakesNavigator navigationProvider,
                          @NonNull BaseSchedulerProvider schedulerProvider) {
        mNavigator = navigationProvider;
        mRepository = ratesRepository;
        mSchedulerProvider = schedulerProvider;

        mLoadingIndicatorSubject = BehaviorSubject.create(false);
        mSnackbarText = PublishSubject.create();
    }


    /**
     * @return the model for the quakes list.
     */
    @NonNull
    public Observable<QuakesUiModel> getUiModel() {
        return getQuakeItems()
                .doOnSubscribe(() -> mLoadingIndicatorSubject.onNext(true))
                .doOnNext(__ -> mLoadingIndicatorSubject.onNext(false))
                .doOnError(__ -> mSnackbarText.onNext(R.string.loading_quakes_error))
                .map(this::constructQuakesModel);
    }

    @NonNull
    private QuakesUiModel constructQuakesModel(List<QuakeItem> quakes) {
        boolean isQuakesListVisible = !quakes.isEmpty();
        boolean isNoQuakesViewVisible = !isQuakesListVisible;
        NoQuakesModel noQuakesModel = null;
        if (quakes.isEmpty()) {
            noQuakesModel = getNoQuakesModel();
        }

        return new QuakesUiModel(isQuakesListVisible, quakes, isNoQuakesViewVisible,
                noQuakesModel);
    }

    private NoQuakesModel getNoQuakesModel() {
        return new NoQuakesModel(R.string.no_quakes);
    }

    private Observable<List<QuakeItem>> getQuakeItems() {

        return mRepository.getQuakes()
                        .flatMap(list -> Observable.from(list)
                                .map(this::constructQuakeItem).toList());
    }

    private QuakeItem constructQuakeItem(Quake quake){
        return new QuakeItem(quake, () -> handleQuakeClicked(quake));
    }

    private void handleQuakeClicked(Quake quake) {
        // TODO
        mNavigator.addNewAction();
    }
}
