package com.example.weatherapplication.model.api

import com.example.weatherapplication.model.dataClasses.ErrorResponse
import com.example.weatherapplication.model.dataClasses.WeatherResponse
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Deferred


/**
 * ApiRepo
 *
 */

interface ApiRepo {
    fun getWeatherData(
        lat: String?,
        lon: String?,
        query:String?
    ): Deferred<NetworkResponse<WeatherResponse, ErrorResponse>>
}