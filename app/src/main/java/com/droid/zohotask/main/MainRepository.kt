package com.droid.zohotask.main

import com.droid.zohotask.model.userresponse.Result
import com.droid.zohotask.model.userresponse.UserResponseItem
import com.droid.zohotask.model.weather.WResponse
import com.droid.zohotask.utils.Resource

/**
 * Created by SARATH on 17-07-2021
 */
interface MainRepository {

    suspend fun getUserList(count : Int) : Resource<UserResponseItem>

    suspend fun getWeather(lat : Int, lon : Int, appId : String) : Resource<WResponse>

    suspend fun getUserListSizeFromDB() : Resource<List<Result>>

    suspend fun insertUserItem(result: Result) : Boolean
}