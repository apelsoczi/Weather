package com.example.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.ui.WeatherViewIntent.ViewOnCreated
import com.example.weatherapp.ui.WeatherViewState.Initial
import com.example.weatherapp.ui.WeatherViewState.WeatherSearchResult
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class WeatherViewModel : ViewModel() {

    val repository = WeatherRepository

    private val _state = MutableStateFlow<WeatherViewState>(Initial)
    val state = _state

    private val _events = MutableSharedFlow<WeatherViewEvent>()
    val events = _events

    fun handle(intent: WeatherViewIntent) {
        when (intent) {
            is ViewOnCreated -> loadCurrentWeather()
        }
    }

    private fun loadCurrentWeather() = viewModelScope.launch(IO) {
        val current = repository.currentWeather() ?: return@launch
        val forecast = repository.weatherForecast()
        val news = repository.news()

        _state.value = WeatherSearchResult(current, forecast, news)
    }

}