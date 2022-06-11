package com.example.weatherapplication.model

import android.util.Log
import com.example.weatherapplication.R
import com.example.weatherapplication.model.api.ApiRepo
import com.example.weatherapplication.model.dataClasses.ErrorResponse
import com.example.weatherapplication.model.dataClasses.WeatherResponse
import com.haroldadmin.cnradapter.NetworkResponse
import java.io.IOException


class ModelRepository(
    private val _apiRepository: ApiRepo
) : ModelRepo {

    override suspend fun getWeatherData(
        lat: String?,
        lon: String?,
        query:String?
    ): NetworkResponse<WeatherResponse, ErrorResponse> {
        return when (val response = _apiRepository.getWeatherData(lat, lon,query).await()) {
            is NetworkResponse.Success -> response
            else -> {
                Log.v("response","$response")
                handleError(response)
            }
        }
    }
    /**
     * Handle error while API call.
     * Search instead in local database and return the offline results.
     */
    private fun <T : Any> handleError(response: NetworkResponse<T, ErrorResponse>): NetworkResponse<T, ErrorResponse> {
        return when (response) {
            is NetworkResponse.ServerError -> when (response.code) {
                500 -> response.apply { body?.errorMessageId = R.string.server_error }
                401 -> response.apply { body?.errorMessageId = R.string.session_expired }
                else -> response
            }

            is NetworkResponse.NetworkError -> handleNetworkError(response.error)
            is NetworkResponse.UnknownError -> handleUnknownError(response.error)
            else -> response
        }
    }

    /**
     * Handle network error through converting the network error to server error.
     *
     * @param error The error happened for log purpose.
     */
    private fun <T : Any> handleNetworkError(
        error: IOException,
    ): NetworkResponse<T, ErrorResponse> {
        return NetworkResponse.ServerError(
            ErrorResponse(errorMessageId = R.string.connection_error), 0
        )
    }

    /**
     * Handle unknown error through converting the unknown error to server error.
     *
     * @param error The error happened for log purpose.
     */
    private fun <T : Any> handleUnknownError(error: Throwable): NetworkResponse<T, ErrorResponse> {
        return NetworkResponse.ServerError(
            ErrorResponse(errorMessageId = R.string.unknown_error), 0
        )
    }

    companion object {
        const val BASE_URL="https://api.openweathermap.org/data/2.5/"
    }

}