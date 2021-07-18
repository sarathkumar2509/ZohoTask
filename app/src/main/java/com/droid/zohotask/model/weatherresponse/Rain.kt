package com.droid.zohotask.model.weatherresponse

import com.google.gson.annotations.SerializedName

/**
 * Created by SARATH on 18-07-2021
 */
data class Rain (
    @SerializedName("3h")
    var h3: Float = 0.toFloat()
)