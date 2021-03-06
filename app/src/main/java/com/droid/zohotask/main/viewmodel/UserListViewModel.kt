package com.droid.zohotask.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droid.zohotask.main.MainRepository
import com.droid.zohotask.model.userresponse.Result
import com.droid.zohotask.utils.Constants
import com.droid.zohotask.utils.DispatcherProvider
import com.droid.zohotask.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by SARATH on 17-07-2021
 */
class UserListViewModel @ViewModelInject constructor(
    private val dispatchers: DispatcherProvider,
    private val repository: MainRepository
) : ViewModel(){

    sealed class UserListEvent {
        class Success(val result: List<Result>) : UserListEvent()
        class Failure(val error: String) : UserListEvent()
        object Loading : UserListEvent()
        object Empty : UserListEvent()
    }

    sealed class SearchUserEvent {
        class Success(val result: List<Result>) : SearchUserEvent()
        class Failure(val error: String) : SearchUserEvent()
        object Loading : SearchUserEvent()
        object Empty : SearchUserEvent()
    }
    private val random = Random()
    var userListCount = 1
    private val _userList = MutableStateFlow<UserListEvent>(UserListEvent.Empty)
    val userList: StateFlow<UserListEvent> = _userList

    private val _searchList = MutableStateFlow<SearchUserEvent>(SearchUserEvent.Empty)
    val searchList: StateFlow<SearchUserEvent> = _searchList
     var  existingList : MutableList<Result>? = null
    private fun getRandomInt(max : Int, min : Int) : Int{
        return random.nextInt(max - min ) + min
    }
    fun getUserList(count : Int){
        viewModelScope.launch(dispatchers.io) {
            _userList.value = UserListEvent.Loading

//            if (repository.getUserListSizeFromDB().data?.size != null){
//
//                when(val userListResponse = repository.getUserListSizeFromDB()){
//                    is Resource.Error ->{
//                        _userList.value = UserListEvent.Failure(userListResponse.message!!)
//                    }
//
//                    is Resource.Success ->{
//                        val data =userListResponse.data
//                                if (data == null) {
//                                _userList.value = UserListEvent.Failure("UnExpected Error")
//                                } else {
//                                _userList.value = UserListEvent.Success(data)
//                                }
//                    }
//                }
//            }else{
                when(val userListResponse = repository.getUserList(Constants.QUERY_PAGE_SIZE)){
                    is Resource.Error ->{
                        _userList.value = UserListEvent.Failure(userListResponse.message!!)
                    }

                    is Resource.Success ->{
                        val data =userListResponse.data
                        if (data != null) {
                            for (i in data.results) {
                                i.height=getRandomInt(550,200)
                                repository.insertUserItem(i)

                                Log.d("insert ${i.name}", "$i")
                            }

                            if(existingList==null){
                                existingList= data.results
                            }
                            else {
                                existingList!!.addAll(data.results)
                            }
                        }
                        if (data == null) {
                            _userList.value = UserListEvent.Failure("UnExpected Error")
                        } else {
                            _userList.value = UserListEvent.Success(existingList!!)
                        }
                    }
                }
           // }
        }
    }


    fun searchUser(searchQuery : String) {
        viewModelScope.launch(dispatchers.io) {
            _searchList.value = SearchUserEvent.Loading

            when (val userListResponse = repository.searchUser(searchQuery)) {
                is Resource.Error -> {
                    _searchList.value = SearchUserEvent.Failure(userListResponse.message!!)
                }

                is Resource.Success -> {
                    val data = userListResponse.data
                    if (data == null) {
                        _searchList.value = SearchUserEvent.Failure("UnExpected Error")
                    } else {
                        _searchList.value = SearchUserEvent.Success(data)
                    }
                }

            }

        }
    }


}
