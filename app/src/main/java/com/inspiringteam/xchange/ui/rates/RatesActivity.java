package com.inspiringteam.xchange.ui.rates;

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
        RatesFragment fragment = (RatesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = mInjectedFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }
}
