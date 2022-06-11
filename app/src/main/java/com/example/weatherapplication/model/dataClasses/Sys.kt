package com.example.weatherapplication.model.dataClasses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "country")
    var country: String? = null,
    @Json(name = "id")
    var id: Int? = null,
    @Json(name = "sunrise")
    var sunrise: Int? = null,
    @Json(name = "sunset")
    var sunset: Int? = null,
    @Json(name = "type")
    var type: Int? = null
)