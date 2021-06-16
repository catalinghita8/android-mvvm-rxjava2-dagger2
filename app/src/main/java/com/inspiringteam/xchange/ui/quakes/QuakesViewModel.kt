package com.inspiringteam.xchange.ui.quakes

import androidx.lifecycle.ViewModel
import com.inspiringteam.xchange.R
import com.inspiringteam.xchange.data.models.Quake
import com.inspiringteam.xchange.data.source.QuakesRepository
import com.inspiringteam.xchange.di.scopes.AppScoped
import com.inspiringteam.xchange.util.chromeTabsUtils.ChromeTabsWrapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * ViewModel for quakes screen
 */
@AppScoped
class QuakesViewModel @Inject constructor(
    private val repository: QuakesRepository,
    private val tabsWrapper: ChromeTabsWrapper
) : ViewModel() {
    // Use a BehaviourSubject because we are interested in the last object that was emitted before
    // subscribing. We ensure that the loading indicator has the correct visibility.
    private val loadingIndicatorSubject: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    // Use a PublishSubject because we are not interested in the last object that was emitted
    // before subscribing. We avoid displaying the snackBar multiple times.
    private val snackBarTextView: PublishSubject<Int> = PublishSubject.create()

    /**
     * @return a stream of ids that should be displayed in the snackBar
     */
    val snackBarMessage: Observable<Int>
        get() = snackBarTextView.hide()

    /**
     * @return a stream that emits true if the progress indicator should be displayed, false otherwise
     */
    val loadingIndicatorVisibility: Observable<Boolean>
        get() = loadingIndicatorSubject.hide()

    /**
     * @return the model for the quakes screen
     */
    fun getUiModel(isForcedCall: Boolean): Single<QuakesUiModel> {
        return getQuakeItems(isForcedCall)
            .doOnSubscribe { loadingIndicatorSubject.onNext(true) }
            .doOnSuccess { loadingIndicatorSubject.onNext(false) }
            .doOnError { snackBarTextView.onNext(R.string.loading_quakes_error) }
            .map { quakes: List<QuakeItem> -> constructQuakesModel(quakes) }
    }

    fun bindTabsService() = tabsWrapper.unbindCustomTabsService()

    fun unbindTabsService() = tabsWrapper.unbindCustomTabsService()

    private fun getNoQuakesModel() = NoQuakesModel(R.string.no_quakes)

    private fun handleQuakeClicked(quake: Quake) = tabsWrapper.openCustomtab(quake.url)

    private fun getQuakeItems(isForcedCall: Boolean): Single<List<QuakeItem>> {
        if (isForcedCall)
            repository.deleteAllQuakes()
        return repository.getQuakes()
            .flatMap { list: List<Quake>? ->
                Observable
                    .fromIterable(list)
                    .map { quake: Quake -> constructQuakeItem(quake) }.toList()
            }
    }

    private fun constructQuakeItem(quake: Quake): QuakeItem {
        return QuakeItem(quake) { handleQuakeClicked(quake) }
    }

    private fun constructQuakesModel(quakes: List<QuakeItem>): QuakesUiModel {
        val isQuakesListVisible = quakes.isNotEmpty()
        val isNoQuakesViewVisible = !isQuakesListVisible
        var noQuakesModel: NoQuakesModel? = null
        if (quakes.isEmpty())
            noQuakesModel = getNoQuakesModel()
        return QuakesUiModel(
            isQuakesListVisible,
            quakes,
            isNoQuakesViewVisible,
            noQuakesModel
        )
    }

}