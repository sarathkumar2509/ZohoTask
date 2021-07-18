package com.droid.zohotask.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.droid.zohotask.R
import com.droid.zohotask.databinding.FragmentUserDetailBinding
import com.droid.zohotask.model.response.Result

/**
 * Created by SARATH on 18-07-2021
 */
class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {

    private lateinit var binding : FragmentUserDetailBinding

    private val args : UserDetailFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserDetailBinding.bind(view)

        val userDetailItem : Result = args.userDetail
        
        bindUserDetail(userDetailItem)

    }

    private fun bindUserDetail(userDetailItem : Result) {
        Glide.with(this).load(userDetailItem.picture?.large).placeholder(R.drawable.ic_baseline_account_circle_24).circleCrop().into(binding.ivPicture)
        binding.tvFirstName.text = userDetailItem.name!!.first
        binding.tvLastnName.text = userDetailItem.name.last
        binding.tvEmail.text = userDetailItem.email
        binding.tvStreetNumber.text = userDetailItem.location!!.street.number.toString()
        binding.tvStreetName.text = userDetailItem.location.street.name
        binding.tvCity.text = userDetailItem.location.city
        binding.tvState.text = userDetailItem.location.state
        binding.tvCountry.text = userDetailItem.location.country
        binding.tvPostalCode.text = userDetailItem.location.postcode.toString()
        binding.tvAge.text = userDetailItem.dob!!.age.toString()
        binding.tvPhone.text = userDetailItem.phone.toString()
        binding.tvCell.text = userDetailItem.cell.toString()

    }
}