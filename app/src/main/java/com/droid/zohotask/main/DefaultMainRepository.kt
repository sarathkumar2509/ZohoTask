package com.droid.zohotask.main

import android.util.Log
import com.droid.zohotask.model.UserApi
import com.droid.zohotask.model.response.Result
import com.droid.zohotask.model.response.UserResponseItem
import com.droid.zohotask.utils.Resource
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by SARATH on 17-07-2021
 */
class DefaultMainRepository @Inject constructor(
    private val api: UserApi
) : MainRepository{
    override suspend fun getUserList(): Resource<UserResponseItem> {
        return try {
            val response = api.getUserList(5)
            Log.d("DefaultMainRepository","$response")
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Log.d("DefaultMainRepoIf","$result")
                Resource.Success(result)
            } else {
                Log.d("DefaultMainRepoELSE","${response.message()}")
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Log.d("DefaultMainRepoEXP","ERROR ${e.message}")
            Resource.Error(e.message ?: "An Error Occurred")
        }
    }
}