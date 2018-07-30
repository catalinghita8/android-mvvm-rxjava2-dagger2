package com.inspiringteam.xchange.rates;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class RatesActivity extends DaggerAppCompatActivity {
    @Inject
    RatesFragment mInjectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        // Set up fragment
        RatesFragment fragment = (RatesFragment) getFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = mInjectedFragment;
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.contentFrame);
        }
    }
}
