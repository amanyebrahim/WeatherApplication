package com.example.weatherapplication.gps

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class GpsTracker(context: Context?) : Service(), LocationListener {
    private var mContext: Context? = null
    // flag for GPS status
    private var isGPSEnabled = false
    // flag for network status
    var isNetworkEnabled = false
    // flag for GPS status
    //check GPS/wifi enabled
    var canGetLocation = false
    var location: Location? = null
    var latitude = 0.0
    var longitude = 0.0
    var gpsTrakerListner: GPSTrakerListner? = null
    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 100 // 10 meters
    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES = (1000 * 60 // 1 minute
            ).toLong()
    // Declaring a Location Manager
    private var locationManager: LocationManager? = null

    init {
        mContext = context
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        try {
            locationManager = mContext?.getSystemService(LOCATION_SERVICE) as LocationManager
            // getting GPS status
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
            // getting network status
            isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
                Log.e("GPS", "No GPS And Network Enabled")
                canGetLocation = false
            } else {
                canGetLocation = true
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    locationManager?.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        Log.e("GPS", "GPS Enabled")
                        location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        updateGPSCoordinates(location)
                    }
                }
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager?.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )
                        if (locationManager != null) {
                            Log.e("GPS", "Network")
                            location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            updateGPSCoordinates(location)
                        }
                    }
                }
                // check if the dialog of start deticting location is open
                if (latitude == 0.0 && longitude == 0.0) {
                    Log.e("latLng", "lat:$latitude lng:$longitude")
                    gpsTrakerListner?.onStartTracker()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateGPSCoordinates(location: Location?) {
        if (location != null) {
            latitude = location.latitude
            longitude = location.longitude
            Log.e("LatLng:", "Lat:$latitude  Lng:$longitude")
            gpsTrakerListner?.onTrackerSuccess(latitude, longitude)
        } else {
            Log.e("GPS", "Location null")
        }
    }

    fun setGpsTrackerListener(mGpsTrakerListner: GPSTrakerListner){
        gpsTrakerListner=mGpsTrakerListner
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    fun stopUsingGPS() {
        locationManager?.removeUpdates(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        Log.e("GPS", "Location :$location")
        updateGPSCoordinates(location)
    }
   override fun onProviderDisabled(provider: String) {
       Log.e("onProvider", "Disable")
    }

    @SuppressLint("MissingPermission")
    override fun onProviderEnabled(provider: String) {
        Log.e("onProvider", "Enable")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.e("onProvider", "StatusChanged" + provider.toString())
    }

}