package com.example.weatherapp.data.model

import org.json.JSONObject


data class Weather(
    val tempMin: Double,
    val tempMax: Double,
    val icon: String,
    val temp: Double? = null
) {
    companion object {
        fun fromJSON(weatherJSON: JSONObject) =
            Weather(
                weatherJSON.getJSONObject("main").getDouble("temp_min"),
                weatherJSON.getJSONObject("main").getDouble("temp_max"),
                (weatherJSON.getJSONArray("weather").get(0) as JSONObject).getString("icon"),
                weatherJSON.getJSONObject("main").getDouble("temp")
            )
    }
}