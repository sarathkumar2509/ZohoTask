package com.droid.zohotask.main

import com.droid.zohotask.model.useresponse.UserResponseItem
import com.droid.zohotask.model.weatherresponse.WeatherResponse
import com.droid.zohotask.utils.Resource

/**
 * Created by SARATH on 17-07-2021
 */
interface MainRepository {

    suspend fun getUserList() : Resource<UserResponseItem>

    suspend fun getWeather() : Resource<WeatherResponse>
}