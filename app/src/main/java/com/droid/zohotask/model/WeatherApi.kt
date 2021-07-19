package com.droid.zohotask.model

import com.droid.zohotask.model.weather.WResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by SARATH on 18-07-2021
 */
interface WeatherApi {

    @GET("data/2.5/weather?")
    suspend fun getCurrentWeather(
        @Query("lat") lat : Int, @Query("lon") lon : Int, @Query("APPID") appId : String
    ) : Response<WResponse>

}