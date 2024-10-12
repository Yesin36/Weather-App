package com.gogote.weatherapp.Model

import com.google.gson.annotations.SerializedName

data class ForcastResponseApi(
    @SerializedName("list")
    val forecastList: List<ForecastData>
) {
    data class ForecastData(
        @SerializedName("main")
        val main: Main,
        @SerializedName("dt_txt")
        val dtTxt: String,
        @SerializedName("weather")
        val weather: List<Weather>
    )

    data class Main(
        @SerializedName("temp")
        val temp: Double,
        @SerializedName("feels_like")
        val feelsLike: Double,
        @SerializedName("humidity")
        val humidity: Int,
        @SerializedName("pressure")
        val pressure: Int,
        @SerializedName("temp_min")
        val tempMin: Double,
        @SerializedName("temp_max")
        val tempMax: Double
    )

    data class Weather(
        @SerializedName("description")
        val description: String,
        @SerializedName("icon")
        val icon: String,
        @SerializedName("main")
        val main: String
    )
}
