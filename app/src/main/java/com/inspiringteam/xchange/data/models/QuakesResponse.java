package com.inspiringteam.xchange.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class QuakesResponse {
    @SerializedName("features")
    @Expose
    public List<QuakeWrapper> quakeWrapperList;

    public List<QuakeWrapper> getQuakeWrapperList() {
        return quakeWrapperList;
    }

    public void setquakeWrapperList(List<QuakeWrapper> quakeWrapperList) {
        this.quakeWrapperList = quakeWrapperList;
    }
}

