package com.droid.zohotask.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.droid.zohotask.R
import com.droid.zohotask.databinding.FragmentUserListBinding
import com.droid.zohotask.main.MainViewModel
import kotlinx.coroutines.flow.collect

/**
 * Created by SARATH on 17-07-2021
 */
class UserListFragment : Fragment(R.layout.fragment_user_list){

    private lateinit var binding : FragmentUserListBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserListBinding.bind(view)

        viewModel.getUserList()

        lifecycleScope.launchWhenCreated {
            viewModel.userList.collect { event ->
                when(event){
                    is MainViewModel.UserListEvent.Success ->{
                        binding.pbUserListFragment.isVisible = false
                        var result = event.result
                        Log.d("UserListFragment","$result")
                    }
                    is MainViewModel.UserListEvent.Failure->{
                    //Show Toast
                    }
                    is MainViewModel.UserListEvent.Loading->{
                        binding.pbUserListFragment.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }
}