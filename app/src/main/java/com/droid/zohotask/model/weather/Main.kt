package com.droid.zohotask.model.weather

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Main(
    @SerializedName("feels_like")
    val feels_like: Double,
    @SerializedName("grnd_level")
    val grnd_level: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("sea_level")
    val sea_level: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_max")
    val temp_max: Double,
    @SerializedName("temp_min")
    val temp_min: Double
) : Serializable