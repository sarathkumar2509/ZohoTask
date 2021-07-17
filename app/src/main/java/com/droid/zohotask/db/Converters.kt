package com.droid.zohotask.db

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.droid.zohotask.model.response.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by SARATH on 17-07-2021
 */
class Converters {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromDob(dob: Dob) : String?{
        val type = object : TypeToken<Dob>() {}.type
        return Gson().toJson(dob,type)
    }

    @TypeConverter
    fun toDob(dob : String) : Dob{
        val type = object : TypeToken<Dob>() {}.type
        return Gson().fromJson(dob,type)
    }

    @TypeConverter
    fun fromId(id: Id) : String?{
        val type = object : TypeToken<Id>() {}.type
        return Gson().toJson(id,type)
    }

    @TypeConverter
    fun toId(id : String) : Id{
        val type = object : TypeToken<Id>() {}.type
        return Gson().fromJson(id,type)
    }

    @TypeConverter
    fun fromLocation(location: Location) : String?{
        val type = object : TypeToken<Location>() {}.type
        return Gson().toJson(location,type)
    }

    @TypeConverter
    fun toLocation(location : String) : Location{
        val type = object : TypeToken<Location>() {}.type
        return Gson().fromJson(location,type)
    }

    @TypeConverter
    fun fromLogin(login: Login) : String?{
        val type = object : TypeToken<Login>() {}.type
        return Gson().toJson(login,type)
    }

    @TypeConverter
    fun toLogin(login : String) : Login{
        val type = object : TypeToken<Login>() {}.type
        return Gson().fromJson(login,type)
    }

    @TypeConverter
    fun fromName(name: Name) : String?{
        val type = object : TypeToken<Name>() {}.type
        return Gson().toJson(name,type)
    }

    @TypeConverter
    fun toName(name : String) : Name{
        val type = object : TypeToken<Name>() {}.type
        return Gson().fromJson(name,type)
    }

    @TypeConverter
    fun fromPicture(picture: Picture) : String?{
        val type = object : TypeToken<Picture>() {}.type
        return Gson().toJson(picture,type)
    }

    @TypeConverter
    fun toPicture(picture : String) : Picture{
        val type = object : TypeToken<Picture>() {}.type
        return Gson().fromJson(picture,type)
    }


    @TypeConverter
    fun fromRegistered(registered: Registered) : String?{
        val type = object : TypeToken<Registered>() {}.type
        return Gson().toJson(registered,type)
    }

    @TypeConverter
    fun toRegistered(registered : String) : Registered{
        val type = object : TypeToken<Registered>() {}.type
        return Gson().fromJson(registered,type)
    }

    @TypeConverter
    fun fromUserResponseItem(userResponseItem: UserResponseItem) : String?{
        val type = object : TypeToken<UserResponseItem>() {}.type
        return Gson().toJson(userResponseItem,type)
    }

    @TypeConverter
    fun toUserResponseItem(userResponseItem : String) : UserResponseItem{
        val type = object : TypeToken<UserResponseItem>() {}.type
        return Gson().fromJson(userResponseItem,type)
    }


}
