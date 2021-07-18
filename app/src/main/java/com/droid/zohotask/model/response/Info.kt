package com.droid.zohotask.model.response

data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)