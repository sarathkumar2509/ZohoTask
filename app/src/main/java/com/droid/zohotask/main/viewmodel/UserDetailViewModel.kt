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
class UserDetailViewModel @ViewModelInject constructor(
    private val dispatchers: DispatcherProvider,
    private val repository: MainRepository
) : ViewModel(){

    sealed class WeatherEvent{
        class Success(val result: WResponse) : WeatherEvent()
        class Failure(val error: String) : WeatherEvent()
        object Loading : WeatherEvent()
        object Empty : WeatherEvent()
    }

    private val _weather = MutableStateFlow<WeatherEvent>(WeatherEvent.Empty)
    val weather: StateFlow<WeatherEvent> = _weather



    fun getUserWeather(lat : Int, lon : Int, appId : String){
        viewModelScope.launch (dispatchers.io){
            _weather.value=WeatherEvent.Loading

            when(val weatherData = repository.getWeather(lat,lon, appId)){
                is Resource.Error -> {
                    _weather.value = WeatherEvent.Failure(weatherData.message!!)
                }

                is Resource.Success->{
                    val data = weatherData.data
                    if(data==null){
                        _weather.value=WeatherEvent.Failure("Unexpected Error ")
                    }

                    else{
                        _weather.value=WeatherEvent.Success(data)
                    }
                }
            }
        }
    }
}
