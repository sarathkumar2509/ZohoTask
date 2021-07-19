package com.droid.zohotask.main

import android.util.Log
import com.droid.zohotask.db.UserDatabase
import com.droid.zohotask.model.UserApi
import com.droid.zohotask.model.WeatherApi
import com.droid.zohotask.model.userresponse.Result
import com.droid.zohotask.model.userresponse.UserResponseItem
import com.droid.zohotask.model.weather.WResponse
import com.droid.zohotask.utils.Resource
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by SARATH on 17-07-2021
 */
class DefaultMainRepository @Inject constructor(
    private val api: UserApi,
    private val weatherApi: WeatherApi,
    private val database: UserDatabase
) : MainRepository{
    override suspend fun getUserList(count : Int): Resource<UserResponseItem> {
        return try {
            Log.d("DefaultMainRepository","$count")
            val response = api.getUserList(count)

            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An Error Occurred")
        }
    }


    override suspend fun getWeather(lat : Int, lon : Int, appId : String): Resource<WResponse> {
        return try {
            val response = weatherApi.getCurrentWeather(lat,lon,appId)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An Error Occurred")
        }
    }

    override suspend fun getUserListSizeFromDB(): Resource<List<Result>> {
        return try {
            val response = database.getUserListDao().getUserListSizeFromDB()

            if (response.isNotEmpty()) {
                Resource.Success(response)
            } else {
                Resource.Error("No Data in DB")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An Error Occurred")
        }
    }

    override suspend fun insertUserItem(result: Result) : Boolean {
        database.getUserListDao().insert(result)
        return true
    }
}