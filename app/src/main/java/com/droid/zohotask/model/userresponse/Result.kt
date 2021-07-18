package com.droid.zohotask.model.userresponse

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName ="userlist")
data class Result(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("dbid")
    var dbid: Int,
    @SerializedName("cell")
    val cell: String?,
    @SerializedName("dob")
    val dob: Dob?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("id")
    val id: Id?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("login")
    val login: Login?,
    @SerializedName("name")
    val name: Name?,
    @SerializedName("nat")
    val nat: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("picture")
    val picture: Picture?,
    @SerializedName("registered")
    val registered: Registered?
) : Serializable