package com.droid.zohotask.model.response

import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String,
    @SerializedName("title")
    val title: String
)