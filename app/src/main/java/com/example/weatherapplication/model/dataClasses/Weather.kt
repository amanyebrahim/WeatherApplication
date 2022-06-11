package com.example.weatherapplication.model.dataClasses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "description")
    var description: String? = null,
    @Json(name = "icon")
    var icon: String? = null,
    @Json(name = "id")
    var id: Int? = null,
    @Json(name = "main")
    var main: String? = null
)