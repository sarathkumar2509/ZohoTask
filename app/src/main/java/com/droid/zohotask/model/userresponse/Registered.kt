package com.droid.zohotask.model.userresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Registered(
    @SerializedName("age")
    val age: Int,
    @SerializedName("date")
    val date: String
) : Serializable