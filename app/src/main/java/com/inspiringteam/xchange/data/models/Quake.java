package com.inspiringteam.xchange.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.common.base.Objects;


/**
 * Immutable model class for a Quake.
 */
@Entity(tableName = "quakes")
public final class Quake {
    @Ignore
    private static final long STALE_MS = 5 * 60 * 1000; // Data is stale after 5 minutes

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("code")
    @Expose
    private String id;

    @SerializedName("mag")
    @ColumnInfo(name = "mag")
    @Expose
    private Double magnitude;

    @SerializedName("time")
    @ColumnInfo(name = "time")
    @Expose
    private Long timeStamp;

    @ColumnInfo(name = "timeAdded")
    @Expose
    private Long timeStampAdded;

    @SerializedName("place")
    @ColumnInfo(name = "place")
    @Expose
    private String location;

    @SerializedName("url")
    @ColumnInfo(name = "url")
    @Expose
    private String url;

    @SerializedName("sig")
    @ColumnInfo(name = "sig")
    @Expose
    private int gravity;

    @Ignore
    public Quake(String id, String location) {
        this.id = id;
        this.location = location;
    }

    @Ignore
    public Quake(String id, String location, String url) {
        this.id = id;
        this.location = location;
        this.url = url;
    }

    @Ignore
    public Quake(Double magnitude, String location) {
        this.magnitude = magnitude;
        this.location = location;
    }

    @Ignore
    public Quake(Double magnitude, String location, Long timeStamp, Long timeStampAdded) {
        this.magnitude = magnitude;
        this.timeStamp = timeStamp;
        this.location = location;
        this.timeStampAdded = timeStampAdded;
    }

    public Quake(@NonNull String id, Double magnitude, Long timeStamp, Long timeStampAdded, String location, String url, int gravity) {
        this.id = id;
        this.magnitude = magnitude;
        this.timeStamp = timeStamp;
        this.timeStampAdded = timeStampAdded;
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

    public Long getTimeStampAdded() {
        return timeStampAdded;
    }

    public void setTimeStampAdded(Long timeStampAdded) {
        this.timeStampAdded = timeStampAdded;
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

    public boolean isUpToDate() {
        return System.currentTimeMillis() - timeStampAdded < STALE_MS;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, location, magnitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Quake quake = (Quake) obj;

        return Objects.equal(id, quake.id) &&
                Objects.equal(magnitude, quake.magnitude) &&
                Objects.equal(location, quake.location);
    }

    @Override
    public String toString() {
        return location + magnitude;
    }
}
