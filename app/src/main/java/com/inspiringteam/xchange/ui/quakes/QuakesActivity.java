package com.inspiringteam.xchange.ui.quakes;

import android.os.Bundle;

import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class QuakesActivity extends DaggerAppCompatActivity {
    @Inject
    QuakesFragment mInjectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quakes);

        // Set up fragment
        QuakesFragment fragment = (QuakesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = mInjectedFragment;
            ActivityUtils.INSTANCE.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }
}
