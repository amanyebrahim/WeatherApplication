package com.example.weatherapplication.gps


interface GPSTrakerListner {
    fun onTrackerSuccess(lat: Double?, log: Double?)
    fun onStartTracker()
}