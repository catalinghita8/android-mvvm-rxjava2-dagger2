package com.inspiringteam.xchange.rates;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.di.scopes.ActivityScoped;

import javax.inject.Inject;

import dagger.android.DaggerFragment;


@ActivityScoped
public class RatesFragment extends DaggerFragment {

    @Inject
    public RatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rates, container, false);
    }

}
