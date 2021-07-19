package com.droid.zohotask.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droid.zohotask.main.MainRepository
import com.droid.zohotask.model.weather.WResponse
import com.droid.zohotask.utils.DispatcherProvider
import com.droid.zohotask.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by SARATH on 19-07-2021
 */
class BaseViewModel @ViewModelInject constructor(
    private val dispatchers: DispatcherProvider,
    private val repository: MainRepository
) : ViewModel() {

    sealed class UserLocationWeatherEvent{
        class Success(val result: WResponse) : UserLocationWeatherEvent()
        class Failure(val error: String) : UserLocationWeatherEvent()
        object Loading : UserLocationWeatherEvent()
        object Empty : UserLocationWeatherEvent()
    }

    private val _userLocationWeather = MutableStateFlow<UserLocationWeatherEvent>(UserLocationWeatherEvent.Empty)
    val userLocationWeather: StateFlow<UserLocationWeatherEvent> = _userLocationWeather

    fun getUserLocationWeather(lat : Int, lon : Int, appId : String){
        viewModelScope.launch (dispatchers.io){
            _userLocationWeather.value= UserLocationWeatherEvent.Loading

            when(val weatherData = repository.getWeather(lat,lon, appId)){
                is Resource.Error -> {
                    _userLocationWeather.value = UserLocationWeatherEvent.Failure(weatherData.message!!)
                }

                is Resource.Success->{
                    val data = weatherData.data
                        if(data==null){
                           _userLocationWeather.value= UserLocationWeatherEvent.Failure("Unexpected Error ")
                         }

                    else{
                        _userLocationWeather.value= UserLocationWeatherEvent.Success(data)
                    }
                }
            }
        }
    }
}