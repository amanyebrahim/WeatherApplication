package com.example.weatherapplication.model.dataClasses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "deg")
    var deg: Int? = null,
    @Json(name = "gust")
    var gust: Double? = null,
    @Json(name = "speed")
    var speed: Double? = null
)