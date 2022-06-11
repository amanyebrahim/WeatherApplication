package com.example.weatherapplication.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.example.weatherapplication.gps.GPSTrakerListner
import com.example.weatherapplication.gps.GpsTracker
import com.example.weatherapplication.utils.TrackingUtility
import com.example.weatherapplication.utils.TrackingUtility.REQUEST_CODE_LOCATION_PERMISSION
import com.example.weatherapplication.utils.showMessageTwoButtonsDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks, GPSTrakerListner {

    private lateinit var _binding: ActivityMainBinding
    private val _viewModel: MainViewModel by viewModel()
    private var gpsTracker: GpsTracker? = null

    private val _getActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            fetchGps()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        _binding.viewModel = _viewModel
        _binding.lySearch.etSearch.tag = false
        _binding.lySearch.etSearch.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            _binding.lySearch.etSearch.tag = true
        }

        _binding.lySearch.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (_binding.lySearch.etSearch.tag == true) {
                    val searchString = _binding.lySearch.etSearch.text.toString().lowercase().trim()
                    _viewModel.onSearchByCity(searchString)
                }
            }
        })

        requestPermissions()
        observeViewModel()
    }

    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(this)) {
            return
        }
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        fetchGps()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onTrackerSuccess(lat: Double?, log: Double?) {
        Log.v("onTrackerSuccess","$lat $log")
        _viewModel.fetchWeatherData(lat, log)
    }

    override fun onStartTracker() {

    }
    private fun fetchGps() {
        gpsTracker = GpsTracker(this)
        gpsTracker?.setGpsTrackerListener(this)
        when (gpsTracker?.canGetLocation) {
            true ->
                _viewModel.fetchWeatherData(gpsTracker?.latitude, gpsTracker?.longitude)

            else -> {
                showMessageTwoButtonsDialog(getString(R.string.alert_gps)) {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    _getActivityResult.launch(intent)
                }
            }
        }
    }
    private fun observeViewModel(){
        observeErrorViewVisibility()
        observeCityName()
    }
    private fun observeErrorViewVisibility() {
        _viewModel.errorViewVisibility.observe(this) {
            it?.let {
                when (val errorMessageId = _viewModel.errorMessageId.value) {
                    null -> _binding.layoutErrorBinding.tvError.text = _viewModel.errorMessage.value
                    else -> _binding.layoutErrorBinding.tvError.setText(errorMessageId)
                }

                _binding.layoutErrorBinding.layoutError.visibility = it
            }
        }
    }
    private fun observeCityName(){
        _viewModel.cityNameText.observe(this) {
            it?.getContentIfNotHandled()?.let {
                if (_binding.lySearch.etSearch.tag==false) {
                    _binding.lySearch.etSearch.clearFocus()
                    _binding.lySearch.etSearch.setText(it)
                }
            }
        }
    }
}