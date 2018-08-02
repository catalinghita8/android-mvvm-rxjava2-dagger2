package com.inspiringteam.xchange.ui.quakes;

import android.support.annotation.DrawableRes;

import com.inspiringteam.xchange.data.models.Quake;

import rx.functions.Action0;

/**
 * A quake that should be displayed as an item in a list of quake.
 * Contains the quake, the action that should be triggered when taping on the item and the
 * background that should be used for this quake.
 */

final class QuakeItem {
    private Quake mQuake;

//    @DrawableRes
//    private int mBackground;

    private Action0 mOnClickAction;

    public QuakeItem(Quake quake,
                    Action0 onClickAction) {
        mQuake = quake;
//        mBackground = background;
        mOnClickAction = onClickAction;
    }

    public Quake getQuake() {
        return mQuake;
    }

//    public int getBackground() {
//        return mBackground;
//    }

    /**
     * @return the action to be triggered on click events
     */
    public Action0 getOnClickAction() {
        return mOnClickAction;
    }
}
