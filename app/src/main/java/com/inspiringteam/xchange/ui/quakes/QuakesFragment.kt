package com.inspiringteam.xchange.ui.quakes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.inspiringteam.xchange.BaseView
import com.inspiringteam.xchange.R
import com.inspiringteam.xchange.data.models.Quake
import com.inspiringteam.xchange.di.scopes.ActivityScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_quakes.*
import java.util.*
import javax.inject.Inject

@ActivityScoped
class QuakesFragment @Inject constructor() : Fragment(), BaseView {
    private var viewModel: QuakesViewModel? = null
    private var adapter: QuakesAdapter? = null
    private var subscription: CompositeDisposable = CompositeDisposable()

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            QuakesViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = QuakesAdapter(mutableListOf())
        quakesListView.adapter = adapter
        setupSwipeRefreshLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quakes, container, false)
    }

    override fun onResume() {
        super.onResume()
        bindViewModel()
    }

    override fun onPause() {
        super.onPause()
        unbindViewModel()
    }

    override fun bindViewModel() {
        // Use a CompositeDisposable to gather all the subscriptions, so all of them can be
        // later unsubscribed together
        subscription = CompositeDisposable()

        // The ViewModel holds an observable containing the state of the UI.
        // Subscribe to the emissions of the Ui Model
        // Update the view at every emission of the UI Model
        subscription.add(
            viewModel!!.getUiModel(false)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { model: QuakesUiModel -> updateView(model) }
                )  //onError
                { error: Throwable? ->
                    Log.d(TAG, "Error loading quakes")
                    error?.printStackTrace()
                })

        // Subscribe to the emissions of the snackbar text
        subscription.add(
            viewModel!!.snackBarMessage
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { message: Int -> showSnackbar(message) }
                )  //onError
                { error: Throwable? -> Log.d(TAG, "Error showing snackbar", error) })

        // Subscribe to the emissions of the loading indicator visibility
        subscription.add(
            viewModel!!.loadingIndicatorVisibility
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { isVisible: Boolean -> setLoadingIndicatorVisibility(isVisible) }
                )  //onError
                { error: Throwable? -> Log.d(TAG, "Error showing loading indicator", error) })

        // Bind Chrome tabs service
        viewModel!!.bindTabsService()
    }

    override fun unbindViewModel() {
        // Unbind tabs service
        viewModel?.unbindTabsService()
    }

    private fun setupSwipeRefreshLayout() {
        refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(activity!!, R.color.colorPrimary),
            ContextCompat.getColor(activity!!, R.color.colorAccent),
            ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
        )
        // Set the scrolling view in the custom SwipeRefreshLayout.
        refreshLayout.setScrollUpChild(quakesListView)
        refreshLayout.setOnRefreshListener { forceUpdate() }
    }

    private fun forceUpdate() {
        subscription.add(
            viewModel!!.getUiModel(true)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { model: QuakesUiModel -> updateView(model) }
                )  //onError
                { error: Throwable? -> Log.d(TAG, "Error loading quakes") })
    }

    private fun updateView(model: QuakesUiModel) {
        val ratesListVisibility = if (model.isQuakesListVisible) View.VISIBLE else View.GONE
        val noQuakesViewVisibility = if (model.isNoQuakesViewVisible) View.VISIBLE else View.GONE
        quakesLayout.visibility = ratesListVisibility
        noQuakesLayout.visibility = noQuakesViewVisibility
        if (model.isQuakesListVisible)
            showQuakes(model.quakes)

        if (model.isNoQuakesViewVisible && model.noQuakesModel != null)
            showNoQuakes(model.noQuakesModel)
    }

    private fun showNoQuakes(model: NoQuakesModel?) {
        noQuakesFoundTextView.setText(model!!.text)
    }

    private fun showQuakes(quakes: List<QuakeItem>) {
        adapter!!.replaceData(quakes.toMutableList())
    }

    private fun showSnackbar(@StringRes message: Int) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

    private fun setLoadingIndicatorVisibility(isVisible: Boolean) {
        // Make sure isRefreshing is called after the layout is done with everything else.
        refreshLayout.post { refreshLayout.isRefreshing = isVisible }
    }

    companion object {
        private val TAG = QuakesFragment::class.java.simpleName
    }
}