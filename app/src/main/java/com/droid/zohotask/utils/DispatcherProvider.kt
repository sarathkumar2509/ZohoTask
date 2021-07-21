package com.droid.zohotask.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by SARATH on 17-07-2021
 */
interface DispatcherProvider {
    val main : CoroutineDispatcher
    val io : CoroutineDispatcher
    val default : CoroutineDispatcher
    val unconfined : CoroutineDispatcher
}