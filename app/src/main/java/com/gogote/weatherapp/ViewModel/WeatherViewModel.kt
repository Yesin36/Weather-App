package com.gogote.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import com.gogote.weatherapp.Repository.WeatherRepository
import com.gogote.weatherapp.Server.ApiClient
import com.gogote.weatherapp.Server.ApiServices

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat: Double, lon: Double, units: String) =
        repository.getCurrentWeather(lat, lon, units)

    fun loadForecastWeather(lat: Double, lon: Double, units: String) =
        repository.getForecastWeather(lat, lon, units)
}