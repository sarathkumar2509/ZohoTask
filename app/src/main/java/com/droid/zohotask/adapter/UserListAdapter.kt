package com.droid.zohotask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.droid.zohotask.R
import com.droid.zohotask.databinding.UserlistItemBinding
import com.droid.zohotask.listener.OnUserListItemClick
import com.droid.zohotask.model.userresponse.Result

/**
 * Created by SARATH on 17-07-2021
 */
class UserListAdapter(private val context: Context, private val onUserListItemClickListener: OnUserListItemClick) : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>(){


    inner class UserListViewHolder(val binding : UserlistItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.email == newItem.email
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)
    var userListResponse : MutableList<Result>

        get()=differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserListAdapter.UserListViewHolder {
        return UserListViewHolder(UserlistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: UserListAdapter.UserListViewHolder, position: Int) {
        val userListResponse = userListResponse[position]
        holder.binding.apply {

            ivPicture.layoutParams.height = userListResponse.height!!
            Glide.with(context).load(userListResponse.picture?.large) .transition(
                DrawableTransitionOptions.withCrossFade()).placeholder(R.drawable.ic_baseline_account_circle_24).into(ivPicture)
            tvFirstName.text = userListResponse.name?.first
            tvLastnName.text = userListResponse.name?.last
        }
             holder.itemView.setOnClickListener{
                   onUserListItemClickListener.onClick(userListResponse)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
