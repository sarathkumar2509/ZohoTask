package com.droid.zohotask.model.response

import com.google.gson.annotations.SerializedName

data class UserResponseItem(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<Result>
)