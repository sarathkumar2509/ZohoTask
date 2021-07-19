package com.droid.zohotask.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.droid.zohotask.R
import com.droid.zohotask.databinding.FragmentUserDetailBinding
import com.droid.zohotask.main.viewmodel.UserDetailViewModel
import com.droid.zohotask.model.userresponse.Result
import com.droid.zohotask.model.weather.WResponse
import com.droid.zohotask.utils.Constants
import com.droid.zohotask.utils.Constants.AppId
import kotlinx.coroutines.flow.collect
import kotlin.math.roundToInt

/**
 * Created by SARATH on 18-07-2021
 */
class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {

    private lateinit var binding : FragmentUserDetailBinding

    private lateinit var viewModel: UserDetailViewModel


    private val args : UserDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this)[UserDetailViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserDetailBinding.bind(view)

        val userDetailItem : Result = args.userDetail
        
        val isDetailShown = bindUserDetail(userDetailItem)

        val lat : Int = userDetailItem.location!!.coordinates.latitude.toFloat().roundToInt()

        val lon :Int = userDetailItem.location.coordinates.latitude.toFloat().toInt()

        val appId = AppId

        if (isDetailShown){

            viewModel.getUserWeather(lat,lon,appId)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.weather.collect { event ->
                when(event){

                    is UserDetailViewModel.WeatherEvent.Success ->{
                        binding.pbUserDetailWeather.isVisible = false
                        var result = event.result
                        setWeatherData(result)
                    }

                    is UserDetailViewModel.WeatherEvent.Failure->{
                        Toast.makeText(requireContext(),"No Data",Toast.LENGTH_SHORT).show()
                    }

                    is UserDetailViewModel.WeatherEvent.Loading->{
                        binding.pbUserDetailWeather.isVisible = true
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun setWeatherData(result: WResponse) {
        var climate = ""
        var desc = ""
        for (i in result.weather) {
            climate = i.main
            desc = i.description
        }
        Log.d("Climate","$climate")
            when(climate){
                "Clouds" -> {
                    binding.ltMain.setAnimation("sunwithcloud.json")
                    binding.ltMain.playAnimation()
                    binding.ltMain.loop(true)
                    binding.tvWeather.text = climate
                    binding.tvWeatherDescription.text = desc
                }
                "Snow" -> {
                    binding.ltMain.setAnimation("snow.json")
                    binding.ltMain.playAnimation()
                    binding.ltMain.loop(true)
                    binding.tvWeather.text = climate
                    binding.tvWeatherDescription.text = desc
                }
                "Rain" -> {
                    binding.ltMain.setAnimation("rainy.json")
                    binding.ltMain.playAnimation()
                    binding.ltMain.loop(true)
                    binding.tvWeather.text = climate
                    binding.tvWeatherDescription.text = desc
                }
                "Clear" -> {
                    binding.ltMain.setAnimation("clear.json")
                    binding.ltMain.playAnimation()
                    binding.ltMain.loop(true)
                    binding.tvWeather.text = climate
                    binding.tvWeatherDescription.text = desc
                }
                else ->{
                    binding.ltMain.setAnimation("whitecloud.json")
                    binding.ltMain.playAnimation()
                    binding.ltMain.loop(true)
                    binding.tvWeather.text = climate
                    binding.tvWeatherDescription.text = desc
                }
            }

        binding.tvPressure.text = result.main.pressure.toString()+Constants.PressureUnit
        binding.tvHumidity.text = result.main.humidity.toString()+Constants.HumidityUnit
        binding.tvTemperature.text = (result.main.temp - 273.15).toFloat().toString() + 0x00B0.toChar() + Constants.TemperatureUnit
        binding.tvWindSpeed.text = result.wind.speed.toString() + Constants.WindSpeedUnit

    }

    private fun bindUserDetail(userDetailItem : Result) : Boolean{
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

        return true

    }
}