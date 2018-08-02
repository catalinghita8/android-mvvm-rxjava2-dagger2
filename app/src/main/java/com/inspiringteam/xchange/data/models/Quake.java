package com.inspiringteam.xchange.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.common.base.Objects;
/**
 * Immutable model class for a Task.
 */

public final class Quake {
    @SerializedName("code")
    @Expose
    private String id;

    @SerializedName("mag")
    @Expose
    private Double magnitude;

    @SerializedName("time")
    @Expose
    private Long timeStamp;

    @SerializedName("place")
    @Expose
    private String location;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("sig")
    @Expose
    private int gravity;

    public Quake(Double magnitude, String location) {
        this.magnitude = magnitude;
        this.location = location;
    }

    public Quake(String id, Double magnitude, Long timeStamp, String location, String url, int gravity) {
        this.id = id;
        this.magnitude = magnitude;
        this.timeStamp = timeStamp;
        this.location = location;
        this.url = url;
        this.gravity = gravity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, location, magnitude);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Quake quake = (Quake) obj;

        return Objects.equal(id, quake.id) &&
                Objects.equal(magnitude, quake.magnitude)&&
                        Objects.equal(location, quake.location);
    }

    @Override
    public String toString() {
        return location + magnitude;
    }
}
