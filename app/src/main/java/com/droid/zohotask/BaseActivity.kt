package com.droid.zohotask

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.droid.zohotask.databinding.ActivityBaseBinding
import com.droid.zohotask.main.viewmodel.BaseViewModel
import com.droid.zohotask.main.viewmodel.UserDetailViewModel
import com.droid.zohotask.main.viewmodel.UserListViewModel
import com.droid.zohotask.model.weather.WResponse
import com.droid.zohotask.utils.Constants
import com.droid.zohotask.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.droid.zohotask.utils.Permissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class BaseActivity : AppCompatActivity() ,EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityBaseBinding

    private lateinit var viewModel : BaseViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var currentWeather: TextView

    private lateinit var currentPlace: TextView

    private lateinit var progressBar: ProgressBar

    private lateinit var weatherImage : LottieAnimationView

    private var lat : Int = 0

    private var lon : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_ZohoTask)
        setContentView(binding.root)

        viewModel = this.run {
            ViewModelProvider(this)[BaseViewModel::class.java]
        }

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_title)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        currentWeather = this.findViewById<TextView>(R.id.tvTitleWeather)
        currentPlace = this.findViewById<TextView>(R.id.tvLocation)
        weatherImage = this.findViewById(R.id.ltTitleImage)
        progressBar = this.findViewById(R.id.pbTitle)

        requestPermission()

        viewModel.getUserLocationWeather(lat,lon,Constants.AppId)

        lifecycleScope.launchWhenCreated {
            viewModel.userLocationWeather.collect { event ->
                when(event){
                    is BaseViewModel.UserLocationWeatherEvent.Success ->{
                        var result = event.result
                        setWeatherData(result)
                    }

                    is BaseViewModel.UserLocationWeatherEvent.Failure->{
                        Toast.makeText(this@BaseActivity,"No Data",Toast.LENGTH_SHORT).show()
                    }

                    is BaseViewModel.UserLocationWeatherEvent.Loading->{
                        progressBar.isVisible = true
                    }

                    else -> Unit
                }
            }
        }

    }

    private fun setWeatherData(result: WResponse) {
        var degree = (result.main.temp - 273.15).toFloat().toString() + 0x00B0.toChar() + Constants.TemperatureUnit
        var cityName = result.name
        currentPlace.text = degree + cityName
        currentWeather.text = result.weather[0].description


        var climate = ""
        var desc = ""
        for (i in result.weather) {
            climate = i.main
            desc = i.description
        }
        Log.d("Climate","$climate")
        when(climate){
            "Clouds" -> {
                weatherImage.setAnimation("sunwithcloud.json")
                weatherImage.playAnimation()
                weatherImage.loop(true)
                progressBar.isVisible = false
            }
            "Snow" -> {
                weatherImage.setAnimation("snow.json")
                weatherImage.playAnimation()
                weatherImage.loop(true)
                progressBar.isVisible = false
            }
            "Rain" -> {
                weatherImage.setAnimation("rainy.json")
                weatherImage.playAnimation()
                weatherImage.loop(true)
                progressBar.isVisible = false
            }
            "Clear" -> {
                weatherImage.setAnimation("clear.json")
                weatherImage.playAnimation()
                weatherImage.loop(true)
                progressBar.isVisible = false
            }
            else ->{
                weatherImage.setAnimation("whitecloud.json")
                weatherImage.playAnimation()
                weatherImage.loop(true)
                progressBar.isVisible = false
            }
        }
    }

    private fun requestPermission() {
        if (Permissions.hasLocationPermission(this)) {
            Log.d("requestPermissionPER", "${Permissions.hasLocationPermission(this)}")
            getLocations()
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this, "Location Permission Always Needed to Know Your Location",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else{
            EasyPermissions.requestPermissions(this,"Location Permission Always Needed to Know Your Location",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (ContextCompat.checkSelfPermission(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_BACKGROUND_LOCATION).toString()
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocations()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("MissingPermission")
    private fun getLocations() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show()
            } else {
                it.apply {
                    lat = it.latitude.toInt()
                    lon = it.longitude.toInt()
                }
            }

        }
    }

}