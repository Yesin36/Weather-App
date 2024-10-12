package com.gogote.weatherapp.Server

import com.gogote.weatherapp.Model.CityResponseApi
import com.gogote.weatherapp.Model.CurrentResponseApi
import com.gogote.weatherapp.Model.ForcastResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("data/2.5/weather")

    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String

    ): Call<CurrentResponseApi>

    @GET("data/2.5/forecast")

    fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String

    ): Call<ForcastResponseApi>

    @GET("geo/1.0/direct")
    fun getCityList(
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String

    ): Call<CityResponseApi>


}