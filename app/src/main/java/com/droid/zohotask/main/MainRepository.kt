package com.droid.zohotask.main

import com.droid.zohotask.model.response.Result
import com.droid.zohotask.model.response.UserResponseItem
import com.droid.zohotask.utils.Resource

/**
 * Created by SARATH on 17-07-2021
 */
interface MainRepository {

    suspend fun getUserList() : Resource<UserResponseItem>
}