package com.droid.zohotask.fragment

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.droid.zohotask.R
import com.droid.zohotask.adapter.UserListAdapter
import com.droid.zohotask.databinding.FragmentUserListBinding
import com.droid.zohotask.listener.OnUserListItemClick
import com.droid.zohotask.main.viewmodel.UserListViewModel
import com.droid.zohotask.model.userresponse.Result
import kotlinx.coroutines.flow.collect

/**
 * Created by SARATH on 17-07-2021
 */
class UserListFragment : Fragment(R.layout.fragment_user_list){

    private lateinit var binding : FragmentUserListBinding

    private lateinit var viewModel: UserListViewModel

    private lateinit var userListAdapter: UserListAdapter

    private var state : Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this)[UserListViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserListBinding.bind(view)

        viewModel.getUserList(11)

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            viewModel.userList.collect { event ->
                when(event){

                    is UserListViewModel.UserListEvent.Success ->{
                        binding.pbUserListFragment.isVisible = false
                        var result = event.result
                        userListAdapter.userListResponse = result.toMutableList()
                    }

                    is UserListViewModel.UserListEvent.Failure->{
                            viewModel.getUserList(11)
                    }

                    is UserListViewModel.UserListEvent.Loading->{
                        binding.pbUserListFragment.isVisible = true
                    }

                    else -> Unit
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()
        state = binding.rvUserList.layoutManager?.onSaveInstanceState()!!
    }

    override fun onResume() {
        super.onResume()
        binding.rvUserList.layoutManager?.onRestoreInstanceState(state)
    }


    private fun setupRecyclerView() {
            binding.rvUserList.apply {
                userListAdapter = UserListAdapter(context,object : OnUserListItemClick{

                    override fun onClick(result: Result) {

                        val bundle = Bundle().apply {
                            putSerializable("userDetail",result)
                        }
                            findNavController().navigate(
                               R.id.action_userListFragment_to_userDetailFragment,
                               bundle
                            )
                    }
                })

                adapter = userListAdapter
                layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            }
    }
}