package com.droid.zohotask.model.userresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Timezone(
    @SerializedName("description")
    val description: String,
    @SerializedName("offset")
    val offset: String
) : Serializable