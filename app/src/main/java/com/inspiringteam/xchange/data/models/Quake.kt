package com.inspiringteam.xchange.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.common.base.Objects
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Immutable model class for a Quake.
 */
@Entity(tableName = "quakes")
class Quake {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("code")
    @Expose
    var id: String = ""

    @SerializedName("mag")
    @ColumnInfo(name = "mag")
    @Expose
    var magnitude: Double? = null

    @SerializedName("time")
    @ColumnInfo(name = "time")
    @Expose
    var timeStamp: Long? = null

    @ColumnInfo(name = "timeAdded")
    @Expose
    var timeStampAdded: Long? = null

    @SerializedName("place")
    @ColumnInfo(name = "place")
    @Expose
    var location: String

    @SerializedName("url")
    @ColumnInfo(name = "url")
    @Expose
    var url: String? = null

    @SerializedName("sig")
    @ColumnInfo(name = "sig")
    @Expose
    var gravity: Int = 0

    @Ignore
    constructor(id: String, location: String) {
        this.id = id
        this.location = location
    }

    @Ignore
    constructor(id: String, location: String, url: String?) {
        this.id = id
        this.location = location
        this.url = url
    }

    @Ignore
    constructor(magnitude: Double?, location: String) {
        this.magnitude = magnitude
        this.location = location
    }

    @Ignore
    constructor(magnitude: Double?, location: String, timeStamp: Long?, timeStampAdded: Long?) {
        this.magnitude = magnitude
        this.timeStamp = timeStamp
        this.location = location
        this.timeStampAdded = timeStampAdded
    }

    constructor(
        id: String,
        magnitude: Double?,
        timeStamp: Long?,
        timeStampAdded: Long?,
        location: String,
        url: String?,
        gravity: Int
    ) {
        this.id = id
        this.magnitude = magnitude
        this.timeStamp = timeStamp
        this.timeStampAdded = timeStampAdded
        this.location = location
        this.url = url
        this.gravity = gravity
    }

    val isUpToDate: Boolean
        get() {
            return System.currentTimeMillis() - (timeStampAdded)!! < STALE_MS
        }

    public override fun hashCode(): Int {
        return Objects.hashCode(id, location, magnitude)
    }

    public override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null || javaClass != obj.javaClass) return false
        val quake: Quake = obj as Quake
        return (Objects.equal(id, quake.id) &&
                Objects.equal(magnitude, quake.magnitude) &&
                Objects.equal(location, quake.location))
    }

    public override fun toString(): String {
        return location + magnitude
    }

    companion object {
        @Ignore
        private val STALE_MS: Long = (5 * 60 * 1000 // Data is stale after 5 minutes
                ).toLong()
    }
}