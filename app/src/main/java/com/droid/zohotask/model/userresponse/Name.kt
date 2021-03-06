package com.droid.zohotask.model.userresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Name(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String,
    @SerializedName("title")
    val title: String
) : Serializable