package com.droid.zohotask.model.userresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Street(
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: Int
) : Serializable