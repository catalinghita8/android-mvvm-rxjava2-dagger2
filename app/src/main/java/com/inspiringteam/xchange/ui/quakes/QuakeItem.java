package com.inspiringteam.xchange.ui.quakes;

import android.support.annotation.DrawableRes;

import com.inspiringteam.xchange.data.models.Quake;

import rx.functions.Action0;

/**
 * A quake that should be displayed as an item in a list of quake.
 * Contains the quake and the action that should be triggered when taping on the item a
 */

final class QuakeItem {
    private Quake mQuake;

    private Action0 mOnClickAction;

    public QuakeItem(Quake quake,
                    Action0 onClickAction) {
        mQuake = quake;
        mOnClickAction = onClickAction;
    }

    public Quake getQuake() {
        return mQuake;
    }


    /**
     * @return the action to be triggered on click events
     */
    public Action0 getOnClickAction() {
        return mOnClickAction;
    }
}
