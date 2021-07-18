package com.droid.zohotask.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droid.zohotask.db.UserDatabase
import com.droid.zohotask.model.response.Result
import com.droid.zohotask.utils.DispatcherProvider
import com.droid.zohotask.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by SARATH on 17-07-2021
 */
class MainViewModel @ViewModelInject constructor(
    private val dispatchers: DispatcherProvider,
    private val repository: MainRepository,
    private val database: UserDatabase
) : ViewModel(){


    //revert database from constructor
    sealed class UserListEvent {
        class Success(val result: List<Result>) : UserListEvent()
        class Failure(val error: String) : UserListEvent()
        object Loading : UserListEvent()
        object Empty : UserListEvent()
    }

    private val _userList = MutableStateFlow<UserListEvent>(UserListEvent.Empty)
    val userList: StateFlow<UserListEvent> = _userList

    fun getUserList(count : Int){
        viewModelScope.launch(dispatchers.io) {
            _userList.value = UserListEvent.Loading

            when(val userListResponse = repository.getUserList(count)){
                    is Resource.Error ->{
                        _userList.value = UserListEvent.Failure(userListResponse.message!!)
                    }
                    is Resource.Success ->{
                        val data =userListResponse.data

                        if (data != null) {
                            for (i in data.results) {
                                database.getUserListDao().insert(i)
                                Log.d("insert ${i.name}", "$i")
                            }

                        }

                        if (data == null) {
                            _userList.value = UserListEvent.Failure("UnExpected Error")
                        } else {
                            _userList.value = UserListEvent.Success(data.results)
                        }
                    }
            }
        }
    }

    //for checking
    fun getsearch(){
        viewModelScope.launch(dispatchers.io) {
            val result = database.getUserListDao().getUserSearchList("'%A%'")
            Log.d("search","$result")
        }
    }

}