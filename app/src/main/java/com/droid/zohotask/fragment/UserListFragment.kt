package com.droid.zohotask.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.droid.zohotask.R
import com.droid.zohotask.adapter.UserListAdapter
import com.droid.zohotask.databinding.FragmentUserListBinding
import com.droid.zohotask.listener.OnUserListItemClick
import com.droid.zohotask.main.viewmodel.UserListViewModel
import com.droid.zohotask.model.userresponse.Result
import com.droid.zohotask.utils.Constants.QUERY_PAGE_SIZE
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by SARATH on 17-07-2021
 */
class UserListFragment : Fragment(R.layout.fragment_user_list){
    var pageno=2
    private lateinit var binding : FragmentUserListBinding

    private lateinit var viewModel: UserListViewModel


    private lateinit var userListAdapter: UserListAdapter

    private var state : Parcelable? = null
    fun showNoNetToast( ) {
        Toast.makeText(this.requireContext(),"No Internet",Toast.LENGTH_LONG).show()
    }

    private fun isConnectedToInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(! isConnectedToInternet(this.requireContext())){
            showNoNetToast()
        }
        viewModel = activity?.run {
            ViewModelProvider(this)[UserListViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserListBinding.bind(view)

        viewModel.getUserList(QUERY_PAGE_SIZE)

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            viewModel.userList.collect { event ->
                when(event){

                    is UserListViewModel.UserListEvent.Success ->{
                        hideProgressBar()
                        var result = event.result
                        userListAdapter.userListResponse = result.toMutableList()
                    }

                    is UserListViewModel.UserListEvent.Failure->{
                        Log.d("SARATH","UserError  ${event.error}")
//                        Toast.makeText(requireContext(),"Data fetch failed - "+event.error,Toast.LENGTH_SHORT).show()
                        viewModel.getUserList(QUERY_PAGE_SIZE)
                    }

                    is UserListViewModel.UserListEvent.Loading->{
                        binding.pbUserListFragment.isVisible = true
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.searchList.collect { event ->
                when(event){

                    is UserListViewModel.SearchUserEvent.Success ->{
                        hideProgressBar()
                        var result = event.result
                        userListAdapter.userListResponse = result.toMutableList()
                    }

                    is UserListViewModel.SearchUserEvent.Failure->{
                        hideProgressBar()
                        Toast.makeText(requireContext(),"No User Found",Toast.LENGTH_SHORT).show()
                    }

                    is UserListViewModel.SearchUserEvent.Loading->{
                        showProgressBar()
                    }

                    else -> Unit
                }
            }
        }

        var job : Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                    editable?.let {
                        if (editable.toString().isNotEmpty()){
                            var query = "%$editable%"
                            viewModel.searchUser(query)
                        }
                        else{
                            viewModel.getUserList(QUERY_PAGE_SIZE)
                        }
                    }
            }
        }
    }

    private fun setupRecyclerView() {
            binding.rvUserList.apply {
                userListAdapter = UserListAdapter(context,object : OnUserListItemClick{

                    override fun onClick(result: Result) {

                        val bundle = Bundle().apply {
                            putSerializable("profileDetail",result)
                        }
                            findNavController().navigate(
                               R.id.action_userListFragment_to_profileDetailFragment,
                               bundle
                            )
                    }
                })

                adapter = userListAdapter
                layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                addOnScrollListener(this@UserListFragment.scrollListener)
            }
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.pbUserListFragment.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.pbUserListFragment.visibility = View.VISIBLE
        isLoading = true
    }



    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager

           var  firstVisibleItemPosition =  IntArray(2)

             layoutManager.findFirstVisibleItemPositions(firstVisibleItemPosition)
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val scrolledItemCount =  firstVisibleItemPosition[0]
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
//            val isAtLastItem = firstVisibleItemPosition[0] + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition[0] >= 0
            val invisibleItemCount = totalItemCount-(scrolledItemCount+visibleItemCount)
            val isEnoughItemNotExists = invisibleItemCount <4
//            val isTotalMoreThanVisible = visibleItemCount < totalItemCount-firstVisibleItemPosition[0] + visibleItemCount
            val shouldPaginate = isNotLoadingAndNotLastPage  && isNotAtBeginning &&
                    isEnoughItemNotExists && isScrolling

            if(shouldPaginate) {
                    viewModel.getUserList(QUERY_PAGE_SIZE)
                    pageno++
                lifecycleScope.launchWhenCreated {
                    viewModel.userList.collect { event ->
                        when(event){

                            is UserListViewModel.UserListEvent.Success ->{
                                hideProgressBar()
                                var result = event.result
                                userListAdapter.differ.submitList(result.toList())
//                                val totalPages = result.size / QUERY_PAGE_SIZE + 2
//                                isLastPage = viewModel.userListCount == totalPages
                                userListAdapter.userListResponse = result.toMutableList()
                            }

                            is UserListViewModel.UserListEvent.Failure->{
//                              Toast.makeText(requireContext(),"Data Fetch Failed",Toast.LENGTH_SHORT).show()
                            }

                            is UserListViewModel.UserListEvent.Loading->{
                                binding.pbUserListFragment.isVisible = true
                            }

                            else -> Unit
                        }
                    }
                }

//                viewModel.getBreakingNews("in",pageno,QUERY_PAGE_SIZE)
                pageno+=1
                isScrolling = false
            } else {
                binding.rvUserList.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

//    private fun search(){
//        var job : Job? = null
//        binding.etSearch.addTextChangedListener { editable ->
//            job?.cancel()
//            job = MainScope().launch {
//                editable?.let {
//                    if (editable.toString().isNotEmpty()){
//                        var query = "%$editable%"
//                        viewModel.searchUser(query)
//                    }
//                }
//            }
//        }
//    }
}
