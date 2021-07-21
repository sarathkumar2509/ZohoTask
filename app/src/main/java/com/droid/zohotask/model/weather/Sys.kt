package com.droid.zohotask.model.weather

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sys(
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int,
    @SerializedName("type")
    val type: Int
) : Serializable