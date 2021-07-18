package com.droid.zohotask.model.weatherresponse

import com.google.gson.annotations.SerializedName

/**
 * Created by SARATH on 18-07-2021
 */
data class Weather(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("main")
    var main: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("icon")
    var icon: String? = null
)
