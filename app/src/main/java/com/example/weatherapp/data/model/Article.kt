package com.example.weatherapp.data.model

import org.json.JSONObject


data class Article(
    val title: String,
    val source: String,
    val image: String
) {
    companion object {
        fun fromJSON(articleJSON: JSONObject) =
            Article(
                articleJSON.getString("title"),
                articleJSON.getJSONObject("source").getString("name"),
                articleJSON.getString("urlToImage")
            )
    }
}