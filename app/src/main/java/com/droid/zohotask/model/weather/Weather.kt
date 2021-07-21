package com.droid.zohotask.model.weather

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    var main: String
) : Serializable