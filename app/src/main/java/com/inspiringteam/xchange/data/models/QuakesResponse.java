package com.inspiringteam.xchange.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class QuakesResponse {
    @SerializedName("features")
    @Expose
    public List<Quake> quakeList;

    public List<Quake> getQuakeList() {
        return quakeList;
    }

    public void setQuakeList(List<Quake> quakeList) {
        this.quakeList = quakeList;
    }
}

