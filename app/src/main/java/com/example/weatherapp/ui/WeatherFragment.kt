package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Article
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.ui.WeatherViewIntent.ViewOnCreated
import com.example.weatherapp.ui.WeatherViewState.Initial
import com.example.weatherapp.ui.WeatherViewState.WeatherSearchResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class WeatherFragment : Fragment() {

    val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is Initial -> { }
                    is WeatherSearchResult -> updateUI(it.current, it.forecast, it.news)
                }
            }
        }

        viewModel.handle(ViewOnCreated)
    }

    private fun updateUI(
        current: Weather,
        forecast: List<Weather>,
        news: List<Article>
    ) {
        view?.let { view ->
            view.findViewById<AppCompatImageView>(R.id.city_icon)?.let {
                Glide.with(it.context)
                    .load("https://openweathermap.org/img/wn/${current.icon}@2x.png")
                    .into(it)
            }
            view.findViewById<AppCompatImageView>(R.id.forecast_tomorrow_icon)?.let {
                Glide.with(it.context)
                    .load("https://openweathermap.org/img/wn/${forecast[0].icon}@2x.png")
                    .into(it)
            }
            view.findViewById<AppCompatImageView>(R.id.forecast_two_days_icon)?.let {
                Glide.with(it.context)
                    .load("https://openweathermap.org/img/wn/${forecast[1].icon}@2x.png")
                    .into(it)
            }
            view.findViewById<AppCompatImageView>(R.id.forecast_three_days_icon)?.let {
                Glide.with(it.context)
                    .load("https://openweathermap.org/img/wn/${forecast[2].icon}@2x.png")
                    .into(it)
            }

            view.findViewById<AppCompatTextView>(R.id.city_name)?.let {
                it.text = "Toronto"
            }
            view.findViewById<AppCompatTextView>(R.id.city_max_min)?.let {
                it.text = "max ${current.tempMax.toInt()} / min ${current.tempMin.toInt()}"
            }
            view.findViewById<AppCompatTextView>(R.id.city_current)?.let {
                it.text = "Current ${current.temp?.toInt()}"
            }

            view.findViewById<AppCompatTextView>(R.id.forecast_tomorrow_label)?.let {
                it.text = "${forecast[0].tempMax.toInt()}/${forecast[0].tempMin.toInt()}"
            }
            view.findViewById<AppCompatTextView>(R.id.forecast_two_days_label)?.let {
                it.text = "${forecast[1].tempMax.toInt()}/${forecast[1].tempMin.toInt()}"
            }
            view.findViewById<AppCompatTextView>(R.id.forecast_three_days_label)?.let {
                it.text = "${forecast[2].tempMax.toInt()}/${forecast[2].tempMin.toInt()}"
            }

            view.findViewById<LinearLayout>(R.id.news_articles)?.let { articlesViewGroup ->
                articlesViewGroup.removeAllViewsInLayout()
                news.forEach { article ->
                    layoutInflater?.inflate(R.layout.item_article, articlesViewGroup, false) ?.let { articleView ->
                        articleView.findViewById<AppCompatImageView>(R.id.article_image)?.let {
                            Glide.with(view.context)
                                .load(article.image)
                                .into(it)
                        }
                        articleView.findViewById<AppCompatTextView>(R.id.article_title)?.let {
                            it.text = article.title
                        }
                        articleView.findViewById<AppCompatTextView>(R.id.article_source)?.let {
                            it.text = article.source
                        }

                        articlesViewGroup.addView(articleView)
                    }
                }
            }
        }
    }

}