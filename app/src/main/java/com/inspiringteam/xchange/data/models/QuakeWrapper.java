package com.inspiringteam.xchange.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class QuakeWrapper {
    @SerializedName("properties")
    @Expose
    public Quake quake;

    public QuakeWrapper(Quake quake) {
        this.quake = quake;
    }

    public Quake getQuake() {
        return quake;
    }

    public void setQuake(Quake quake) {
        this.quake = quake;
    }
}
