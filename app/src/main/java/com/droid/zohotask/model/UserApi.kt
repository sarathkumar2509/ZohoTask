package com.droid.zohotask.model

import com.droid.zohotask.model.useresponse.UserResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by SARATH on 17-07-2021
 */
interface UserApi {

    @GET("api/")
    suspend fun getUserList(
        @Query("results") results : Int
    ) : Response<UserResponseItem>

}