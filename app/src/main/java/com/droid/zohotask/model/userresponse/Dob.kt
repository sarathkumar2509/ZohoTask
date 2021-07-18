package com.droid.zohotask.model.userresponse

import com.google.gson.annotations.SerializedName

data class Dob(
    @SerializedName("age")
    val age: Int,
    @SerializedName("date")
    val date: String
)