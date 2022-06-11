package com.example.weatherapplication.model.dataClasses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Clouds(
    @Json(name = "all")
    var all: Int? = null
)