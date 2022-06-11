package com.example.weatherapplication.model.api

import com.example.weatherapplication.BuildConfig
import com.example.weatherapplication.model.dataClasses.ErrorResponse
import com.example.weatherapplication.model.dataClasses.WeatherResponse
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("weather?appid=${BuildConfig.apiKey}")
    fun getWeatherData(
        @Query("lat") lat:String?,
        @Query("lon")  lon:String?,
        @Query("q") cityName:String?,
    ): Deferred<NetworkResponse<WeatherResponse,ErrorResponse>>
}