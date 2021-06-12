package com.inspiringteam.xchange.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuakeWrapper(@field:Expose @field:SerializedName("properties") var quake: Quake)