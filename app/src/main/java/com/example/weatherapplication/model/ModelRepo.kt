package com.example.weatherapplication.model

import com.example.weatherapplication.model.dataClasses.ErrorResponse
import com.example.weatherapplication.model.dataClasses.WeatherResponse
import com.haroldadmin.cnradapter.NetworkResponse


interface ModelRepo {

   suspend fun getWeatherData(
        lat: String?,
        lon: String?,
        query:String?
    ): NetworkResponse<WeatherResponse, ErrorResponse>

}

