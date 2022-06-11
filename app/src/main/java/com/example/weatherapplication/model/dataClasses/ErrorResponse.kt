package com.example.weatherapplication.model.dataClasses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    var errorMessageId: Int? = null

)