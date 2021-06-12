package com.inspiringteam.xchange.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class QuakesResponse(@field:Expose @field:SerializedName("features") var quakeWrapperList: MutableList<QuakeWrapper>)