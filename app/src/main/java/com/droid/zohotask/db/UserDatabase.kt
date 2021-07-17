package com.droid.zohotask.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.droid.zohotask.model.response.Result
import com.droid.zohotask.model.response.UserResponseItem

/**
 * Created by SARATH on 17-07-2021
 */
@Database(
    entities = [Result::class],
    version = 1
)
@TypeConverters(Converters::class)

abstract class UserDatabase : RoomDatabase(){

        abstract fun getUserListDao() : UserListDao
}