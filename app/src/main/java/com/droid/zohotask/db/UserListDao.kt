package com.droid.zohotask.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.droid.zohotask.model.userresponse.Result

/**
 * Created by SARATH on 17-07-2021
 */
@Dao
interface UserListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: Result)

    @Query("SELECT * FROM userlist WHERE name LIKE :key")
    fun getUserSearchList(key: String) : List<Result>
}