package com.droid.zohotask.model.userresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponseItem(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: MutableList<Result>
) : Serializable