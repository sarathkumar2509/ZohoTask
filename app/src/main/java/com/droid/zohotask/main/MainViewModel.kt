package com.droid.zohotask.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droid.zohotask.model.response.UserResponseItem
import com.droid.zohotask.utils.DispatcherProvider
import com.droid.zohotask.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by SARATH on 17-07-2021
 */
class MainViewModel @ViewModelInject constructor(
    private val dispatchers: DispatcherProvider,
    private val repository: MainRepository
) : ViewModel(){

    sealed class UserListEvent {
        class Success(val result: UserResponseItem) : UserListEvent()
        class Failure(val error: String) : UserListEvent()
        object Loading : UserListEvent()
        object Empty : UserListEvent()
    }

    private val _userList = MutableStateFlow<UserListEvent>(UserListEvent.Empty)
    val userList: StateFlow<UserListEvent> = _userList

    fun getUserList(){
        viewModelScope.launch(dispatchers.io) {
            _userList.value = UserListEvent.Loading

            when(val userListResponse = repository.getUserList()){
                    is Resource.Error ->{
                        _userList.value = UserListEvent.Failure(userListResponse.message!!)
                    }
                    is Resource.Success ->{
                        val data =userListResponse.data

                        if (data == null) {
                            _userList.value = UserListEvent.Failure("UnExpected Error")
                        } else {
                            _userList.value = UserListEvent.Success(data)
                        }
                    }
            }
        }
    }
}