package com.droid.zohotask.main

import android.util.Log
import com.droid.zohotask.model.UserApi
import com.droid.zohotask.model.WeatherApi
import com.droid.zohotask.model.userresponse.UserResponseItem
import com.droid.zohotask.model.weatherresponse.WeatherResponse
import com.droid.zohotask.utils.Constants.AppId
import com.droid.zohotask.utils.Constants.lat
import com.droid.zohotask.utils.Constants.lon
import com.droid.zohotask.utils.Resource
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by SARATH on 17-07-2021
 */
class DefaultMainRepository @Inject constructor(
    private val api: UserApi,
    private val weatherApi: WeatherApi
) : MainRepository{
    override suspend fun getUserList(count : Int): Resource<UserResponseItem> {
        return try {
            Log.d("DefaultMainRepository","$count")
            Log.d("DefaultMainRepository","$count")
            val response = api.getUserList(20)

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



    override suspend fun getWeather(): Resource<WeatherResponse> {
        return try {
            val response = weatherApi.getCurrentWeather(lat,lon,AppId)
            Log.d("DefaultMainRepository","$response")
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Log.d("DefaultMainRepoIf","$result")
                Log.d("DefaultMainRepoIf","${result.name}")
                Resource.Success(result)
            } else {
                Log.d("DefaultMainRepoELSE","${response.message()}")
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Log.d("DefaultMainRepoEXP","ERROR $e")
            Resource.Error(e.message ?: "An Error Occurred")
        }
    }
}