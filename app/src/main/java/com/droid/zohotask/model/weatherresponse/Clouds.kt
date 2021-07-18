package com.droid.zohotask.model.weatherresponse

import com.google.gson.annotations.SerializedName

/**
 * Created by SARATH on 18-07-2021
 */

data class Clouds(
    @SerializedName("all")
    var all: Float = 0.toFloat()
)