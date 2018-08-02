package com.inspiringteam.xchange.ui.quakes;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


@ActivityScoped
public class QuakesFragment extends dagger.android.support.DaggerFragment implements BaseView {

    private static final String TAG = QuakesFragment.class.getSimpleName();

    private QuakesViewModel mViewModel;

    private QuakesAdapter mListAdapter;

    private View mNoQuakesView;

    private TextView mNoQuakesMainView;

    private LinearLayout mQuakesView;

    private CompositeSubscription mSubscription = new CompositeSubscription();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

//        mViewModel.restoreState(savedInstanceState);

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
        // using a CompositeSubscription to gather all the subscriptions, so all of them can be
        // later unsubscribed together
        mSubscription = new CompositeSubscription();

        // The ViewModel holds an observable containing the state of the UI.
        // subscribe to the emissions of the Ui Model
        // update the view at every emission of the Ui Model
        mSubscription.add(mViewModel.getUiModel()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::updateView,
                        //onError
                        error -> Log.d(TAG, "Error loading quakes")
                ));
    }

    @Override
    public void unbindViewModel() {

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
        // TODO
    }

    private void updateView(QuakesUiModel model){
        int ratesListVisibility = model.isQuakesListVisible() ? View.VISIBLE : View.GONE;
        int noTasksViewVisibility = model.isNoQuakesViewVisible() ? View.VISIBLE : View.GONE;
        mQuakesView.setVisibility(ratesListVisibility);
        mNoQuakesView.setVisibility(noTasksViewVisibility);

        if (model.isQuakesListVisible()) {
            showQuakes(model.getItemList());
        }
        if (model.isNoQuakesViewVisible() && model.getNoQuakesModel() != null) {
            showNoTasks(model.getNoQuakesModel());
        }
    }

    private void showNoTasks(NoQuakesModel model) {
        mNoQuakesMainView.setText(model.getText());
    }

    private void showQuakes(List<QuakeItem> quakes) {
        mListAdapter.replaceData(quakes);
    }
}
