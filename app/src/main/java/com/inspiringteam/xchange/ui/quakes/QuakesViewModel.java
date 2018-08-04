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

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;


/**
 * ViewModel for the list of quake.
 */

public final class QuakesViewModel extends ViewModel{
    private static final String TAG = QuakesViewModel.class.getSimpleName();

    @NonNull
    private final QuakesRepository mRepository;

    @NonNull
    private final QuakesNavigator mNavigator;

    // using a BehaviourSubject because we are interested in the last object that was emitted before
    // subscribing. Like this we ensure that the loading indicator has the correct visibility.
    private final BehaviorSubject<Boolean> mLoadingIndicatorSubject;

    // using a PublishSubject because we are not interested in the last object that was emitted
    // before subscribing. Like this we avoid displaying the snackbar multiple times
    @NonNull
    private final PublishSubject<Integer> mSnackbarText;

    @Inject
    public QuakesViewModel(@NonNull QuakesRepository ratesRepository,
                          @NonNull QuakesNavigator navigationProvider) {
        mNavigator = navigationProvider;
        mRepository = ratesRepository;

        mLoadingIndicatorSubject = BehaviorSubject.create();
        mSnackbarText = PublishSubject.create();
    }


    /**
     * @return the model for the quakes list.
     */
    @NonNull
    public Observable<QuakesUiModel> getUiModel() {
        return getQuakeItems()
                .doOnSubscribe(__ -> mLoadingIndicatorSubject.onNext(true))
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
                        .flatMap(list -> Observable.fromIterable(list)
                                .map(this::constructQuakeItem).toList().toObservable());
    }

    private QuakeItem constructQuakeItem(Quake quake){
        return new QuakeItem(quake, () -> handleQuakeClicked(quake));
    }

    /**
     * Trigger a force update of the tasks.
     */
    public Completable forceUpdateQuakes() {
        mLoadingIndicatorSubject.onNext(true);
        return mRepository.refreshQuakes()
                .doOnTerminate(() -> mLoadingIndicatorSubject.onNext(false));
    }

    /**
     * @return a stream of ids that should be displayed in the snackbar.
     */
    @NonNull
    public Observable<Integer> getSnackbarMessage() {
        return mSnackbarText.hide();
    }

    /**
     * @return a stream that emits true if the progress indicator should be displayed, false otherwise.
     */
    @NonNull
    public Observable<Boolean> getLoadingIndicatorVisibility() {
        return mLoadingIndicatorSubject.hide();
    }

    private void handleQuakeClicked(Quake quake) {
        // TODO
        mNavigator.addNewAction();
    }
}
