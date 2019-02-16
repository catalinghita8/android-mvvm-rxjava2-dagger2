package com.inspiringteam.xchange.ui.quakes;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.inspiringteam.xchange.BaseView;
import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.ScrollChildSwipeRefreshLayout;
import com.inspiringteam.xchange.di.scopes.ActivityScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;



@ActivityScoped
public class QuakesFragment extends Fragment implements BaseView {

    private static final String TAG = QuakesFragment.class.getSimpleName();

    private QuakesViewModel mViewModel;

    private QuakesAdapter mListAdapter;

    private View mNoQuakesView;

    private TextView mNoQuakesMainView;

    private LinearLayout mQuakesView;

    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    public QuakesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(QuakesViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_quakes, container, false);

        mListAdapter = new QuakesAdapter(new ArrayList<>());

        // Set up quake view
        ListView listView = root.findViewById(R.id.quakes_list);
        listView.setAdapter(mListAdapter);

        mQuakesView = root.findViewById(R.id.quakesLayout);

        setupNoQuakesView(root);
        setupSwipeRefreshLayout(root, listView);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindViewModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindViewModel();
    }

    @Override
    public void bindViewModel() {
        // using a CompositeDisposable to gather all the subscriptions, so all of them can be
        // later unsubscribed together
        mSubscription = new CompositeDisposable();

        // The ViewModel holds an observable containing the state of the UI.
        // subscribe to the emissions of the Ui Model
        // update the view at every emission of the Ui Model
        mSubscription.add(mViewModel.getUiModel(false)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::updateView,
                        //onError
                        error -> Log.d(TAG, "Error loading quakes")
                ));

        // subscribe to the emissions of the snackbar text
        // every time the snackbar text emits, show the snackbar
        mSubscription.add(mViewModel.getSnackbarMessage()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::showSnackbar,
                        //onError
                        error -> Log.d(TAG, "Error showing snackbar", error)
                ));

        // subscribe to the emissions of the loading indicator visibility
        // for every emission, update the visibility of the loading indicator
        mSubscription.add(mViewModel.getLoadingIndicatorVisibility()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::setLoadingIndicatorVisibility,
                        //onError
                        error -> Log.d(TAG, "Error showing loading indicator", error)
                ));

        // bind tabs service
        mViewModel.bindTabsService();
    }

    @Override
    public void unbindViewModel() {
        // don't forget to unbind tabs service
        mViewModel.unbindTabsService();
    }

    private void setupNoQuakesView(View view) {
        mNoQuakesView = view.findViewById(R.id.noQuakesLayout);
        mNoQuakesMainView = view.findViewById(R.id.noQuakesFoundTextView);

    }

    private void setupSwipeRefreshLayout(View root, ListView listView) {
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    private void forceUpdate() {
        mSubscription.add(mViewModel.getUiModel(true)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::updateView,
                        //onError
                        error -> Log.d(TAG, "Error loading quakes")
                ));
    }

    private void updateView(QuakesUiModel model){
        int ratesListVisibility = model.isQuakesListVisible() ? View.VISIBLE : View.GONE;
        int noQuakesViewVisibility = model.isNoQuakesViewVisible() ? View.VISIBLE : View.GONE;
        mQuakesView.setVisibility(ratesListVisibility);
        mNoQuakesView.setVisibility(noQuakesViewVisibility);

        if (model.isQuakesListVisible()) {
            showQuakes(model.getItemList());
        }
        if (model.isNoQuakesViewVisible() && model.getNoQuakesModel() != null) {
            showNoQuakes(model.getNoQuakesModel());
        }
    }

    private void showNoQuakes(NoQuakesModel model) {
        mNoQuakesMainView.setText(model.getText());
    }

    private void showQuakes(List<QuakeItem> quakes) {
        mListAdapter.replaceData(quakes);
    }

    private void showSnackbar(@StringRes int message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void setLoadingIndicatorVisibility(final boolean isVisible) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(() -> srl.setRefreshing(isVisible));
    }
}
