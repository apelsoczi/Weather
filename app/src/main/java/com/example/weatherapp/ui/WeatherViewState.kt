package com.example.weatherapp.ui

import com.example.weatherapp.data.model.Article
import com.example.weatherapp.data.model.Weather


sealed class WeatherViewState {

    object Initial : WeatherViewState()

    data class WeatherSearchResult(
        val current: Weather,
        val forecast: List<Weather>,
        val news: List<Article>
    ) : WeatherViewState()

}