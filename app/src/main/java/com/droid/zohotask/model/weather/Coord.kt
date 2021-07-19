package com.droid.zohotask.model.weather

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Coord(
    @SerializedName("lat")
    val lat: Int,
    @SerializedName("lon")
    val lon: Int
) : Serializable