package com.inspiringteam.xchange.ui.rates;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.inspiringteam.xchange.BaseView;
import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.di.scopes.ActivityScoped;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;


@ActivityScoped
public class RatesFragment extends dagger.android.support.DaggerFragment implements BaseView {

    private static final String TAG = RatesFragment.class.getSimpleName();

    private RatesViewModel mViewModel;


//    private RatesAdapter mListAdapter;

    private View mNoRatesView;

    private TextView mNoRatesMainView;

    private LinearLayout mRatesView;

    private CompositeSubscription mSubscription = new CompositeSubscription();


    @Inject
    public RatesFragment() {
        // Required empty public constructor
    }


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RatesViewModel.class);
        mViewModel = viewModelFactory.create(RatesViewModel.class);
        int hash = mViewModel.hashCode();
        hash = 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_rates, container, false);

        // Set up rates view
        ListView listView = root.findViewById(R.id.rates_list);
//        listView.setAdapter(mListAdapter);

        mRatesView = root.findViewById(R.id.ratesLayout);

//        setupNoRatesView(root);
//        setupSwipeRefreshLayout(root, listView);
//
//        mViewModel.restoreState(savedInstanceState);

        return root;
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void unbindViewModel() {

    }
}
