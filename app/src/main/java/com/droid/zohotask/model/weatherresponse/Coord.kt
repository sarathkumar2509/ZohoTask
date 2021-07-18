package com.droid.zohotask.model.weatherresponse

import com.google.gson.annotations.SerializedName

/**
 * Created by SARATH on 18-07-2021
 */
data class Coord (
    @SerializedName("lon")
    var lon: Float = 0.toFloat(),
    @SerializedName("lat")
    var lat: Float = 0.toFloat()
)