package com.example.weatherapp.ui


sealed class WeatherViewIntent {

    object ViewOnCreated : WeatherViewIntent()

}