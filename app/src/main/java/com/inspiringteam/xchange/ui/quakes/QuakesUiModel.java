package com.inspiringteam.xchange.ui.quakes;

import androidx.annotation.Nullable;

import java.util.List;

public class QuakesUiModel {

    private final boolean mIsQuakesListVisible;

    private final List<QuakeItem> mItemList;

    private final boolean mIsNoQuakesViewVisible;

    private final NoQuakesModel mNoQuakesModel;

    public QuakesUiModel(boolean isQuakesListVisible, List<QuakeItem> itemList,
                        boolean isNoQuakesViewVisible, NoQuakesModel noQuakesModel) {
        mIsQuakesListVisible = isQuakesListVisible;
        mItemList = itemList;
        mIsNoQuakesViewVisible = isNoQuakesViewVisible;
        mNoQuakesModel = noQuakesModel;
    }

    public boolean isQuakesListVisible() {
        return mIsQuakesListVisible;
    }

    public List<QuakeItem> getItemList() {
        return mItemList;
    }

    public boolean isNoQuakesViewVisible() {
        return mIsNoQuakesViewVisible;
    }

    @Nullable
    public NoQuakesModel getNoQuakesModel() {
        return mNoQuakesModel;
    }
}
