package com.example.weatherapplication.model.dataClasses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "base")
    var base: String? = null,
    @Json(name = "clouds")
    var clouds: Clouds? = null,
    @Json(name = "cod")
    var cod: Int? = null,
    @Json(name = "coord")
    var coord: Coord? = null,
    @Json(name = "dt")
    var dt: Int? = null,
    @Json(name = "id")
    var id: Int? = null,
    @Json(name = "main")
    var main: Main? = null,
    @Json(name = "name")
    var name: String? = null,
    @Json(name = "sys")
    var sys: Sys? = null,
    @Json(name = "timezone")
    var timezone: Int? = null,
    @Json(name = "visibility")
    var visibility: Int? = null,
    @Json(name = "weather")
    var weather: List<Weather>? = null,
    @Json(name = "wind")
    var wind: Wind? = null
)