package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig.NEWS_API
import com.example.weatherapp.BuildConfig.WEATHER_API
import com.example.weatherapp.data.model.Article
import com.example.weatherapp.data.model.Weather
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


object WeatherRepository {

    val client = OkHttpClient()

    suspend fun currentWeather(): Weather? {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Toronto" +
                "&appid=${WEATHER_API}" +
                "&units=metric"

        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null

            val responseBody = response.body?.string() ?: ""
            val weatherJSON = JSONObject(responseBody)

            return Weather.fromJSON(weatherJSON)
        }
    }

    suspend fun weatherForecast(): List<Weather> {
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=Toronto" +
                "&appid=${WEATHER_API}" +
                "&units=metric"

        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return emptyList()

            val responseBody = response.body?.string() ?: ""
            val weatherJSON = JSONObject(responseBody)
            val forecastJSON = listOf(
                weatherJSON.getJSONArray("list")[0] as JSONObject,
                weatherJSON.getJSONArray("list")[1] as JSONObject,
                weatherJSON.getJSONArray("list")[2] as JSONObject
            ).map { Weather.fromJSON(it) }

            return forecastJSON
        }
    }

    suspend fun news(): List<Article> {
        val url = "https://newsapi.org/v2/everything?q=Toronto" +
                "&apiKey=${NEWS_API}"

        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return emptyList()

            val responseBody = response.body?.string() ?: ""
            val newsJSON = JSONObject(responseBody)
            val articleJSON = newsJSON.getJSONArray("articles")

            val articles = mutableListOf<JSONObject>()
            for (i in 0..articleJSON.length()-1) {
                articles.add(articleJSON[i] as JSONObject)
            }

            return articles.map { Article.fromJSON(it) }
        }
    }

}