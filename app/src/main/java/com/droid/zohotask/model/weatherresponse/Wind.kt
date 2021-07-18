package com.droid.zohotask.model.weatherresponse

import com.google.gson.annotations.SerializedName

/**
 * Created by SARATH on 18-07-2021
 */
data class Wind (
    @SerializedName("speed")
    var speed: Float = 0.toFloat(),
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
)