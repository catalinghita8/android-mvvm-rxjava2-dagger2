package com.inspiringteam.xchange.ui.quakes;

import androidx.annotation.StringRes;

/**
 * The string  that should be displayed when there are no quakes.
 */
public final class NoQuakesModel {
    @StringRes
    private int mText;

    public NoQuakesModel(int mText) {
        this.mText = mText;
    }

    @StringRes
    public int getText() {
        return mText;
    }

    public void setText(int mText) {
        this.mText = mText;
    }
}
