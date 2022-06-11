package com.example.weatherapplication.ui.main

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.R
import com.example.weatherapplication.model.ModelRepo
import com.example.weatherapplication.model.dataClasses.ErrorResponse
import com.example.weatherapplication.model.dataClasses.WeatherResponse
import com.example.weatherapplication.utils.Event
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val _model: ModelRepo) : ViewModel() {

    private val _errorMessageId = MutableLiveData<Int?>()

    val errorMessageId: LiveData<Int?>
        get() = _errorMessageId

    private val _errorMessage = MutableLiveData<String?>()

    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _errorViewVisibility = MutableLiveData<Int?>()

    val errorViewVisibility: LiveData<Int?>
        get() = _errorViewVisibility

    private val _formVisibility: MutableLiveData<Int?> = MutableLiveData()

    val formVisibility: LiveData<Int?>
        get() = _formVisibility

    private val _loadingVisibility: MutableLiveData<Int?> = MutableLiveData()

    val loadingVisibility: LiveData<Int?>
        get() = _loadingVisibility

    private val _weatherText = MutableLiveData<String?>()

    val weatherText: LiveData<String?>
        get() = _weatherText

    private val _tempText = MutableLiveData<String?>()

    val tempText: LiveData<String?>
        get() = _tempText

    private val _windText = MutableLiveData<String?>()

    val windText: LiveData<String?>
        get() = _windText

    private val _pressureText = MutableLiveData<String?>()

    val pressureText: LiveData<String?>
        get() = _pressureText

    private val _cityNameText = MutableLiveData<Event<String?>>()

    val cityNameText: LiveData<Event<String?>>
        get() = _cityNameText


    private var latitude:Double ?= null
    private var longitude:Double ?= null
    private var query:String?=null

    init {
        showLoading(false)
    }

    fun fetchWeatherData(lat:Double?,long:Double?){
        latitude = lat
        longitude = long
        if(latitude?:0.0 >0.0 && longitude?:0.0 > 0.0)
        getWeatherData()
    }

    fun onSearchByCity(querySearch: String?) {
        query= querySearch
        getWeatherData()
    }


    private fun showLoading(show: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _loadingVisibility.value = when (show) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
    private fun showForm(show: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _formVisibility.value = when (show) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun showErrorView(show: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _errorViewVisibility.value = when (show) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun showErrorView(errorMessageId: Int? = null, errorMessage: String? = null) {
        viewModelScope.launch(Dispatchers.Main) {
            _errorMessageId.value = errorMessageId
            _errorMessage.value = errorMessage
            showErrorView(true)
        }
    }


    private fun getWeatherData() {
        showLoading(true)
        showForm(false)
        showErrorView(false)
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = _model.getWeatherData(latitude.toString(),longitude.toString(),query)) {
                is NetworkResponse.Success -> handleWeatherResponse(
                    response.code,
                    response.body
                )

                is NetworkResponse.ServerError -> when {
                    response.body != null && response.body is ErrorResponse ->
                        handleWeatherErrorResponse(
                            response.code,
                            response.body as ErrorResponse
                        )

                    else -> handleWeatheUnKnownError()
                }

                else -> handleWeatheUnKnownError()
            }
            showLoading(false)
        }
    }

    private fun handleWeatherResponse(statusCode: Int, response: WeatherResponse) {
        Log.v("handleListResponse","$statusCode")
        showForm(true)
        showErrorView(false)
        viewModelScope.launch(Dispatchers.Main) {
            _weatherText.value = if(response.weather?.isNotEmpty() == true) response.weather?.get(0)?.description else ""
            _windText.value = response.wind?.speed.toString()
            _tempText.value = response.main?.temp.toString()
            _pressureText.value = response.main?.pressure.toString()
            _cityNameText.value = Event(response.name)
        }
    }

    private fun handleWeatherErrorResponse(statusCode: Int, response: ErrorResponse) {
        Log.v("handleErrorResponse","$statusCode")
        when (statusCode) {
            0, 500 -> showErrorView(response.errorMessageId)
            else -> showErrorView(errorMessageId = R.string.unknown_error)
        }
    }
    private fun handleWeatheUnKnownError() {
        Log.v("handleUnKnown","Error")
        showErrorView(errorMessageId = R.string.unknown_error)
    }

}