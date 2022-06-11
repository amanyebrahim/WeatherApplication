package com.example.weatherapplication.model.api

import com.example.weatherapplication.model.dataClasses.ErrorResponse
import com.example.weatherapplication.model.dataClasses.WeatherResponse
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Deferred



class ApiRepository(private val _apiService: ApiService) : ApiRepo {
    override fun getWeatherData(
        lat: String?,
        lon: String?,
        query:String?
    ): Deferred<NetworkResponse<WeatherResponse, ErrorResponse>> =
        _apiService.getWeatherData(lat, lon,query)
}