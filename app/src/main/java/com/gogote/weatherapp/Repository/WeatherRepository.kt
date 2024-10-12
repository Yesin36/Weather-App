package com.gogote.weatherapp.Repository



import com.gogote.weatherapp.Server.ApiServices

class WeatherRepository(val api: ApiServices)  {
     fun getCurrentWeather(lat: Double, lon: Double, units: String) =
         api.getCurrentWeather(lat, lon, units, "40637b146eacc23b75f677c04e379d68")

    fun getForecastWeather(lat: Double, lon: Double, units: String) =
        api.getForecastWeather(lat, lon, units, "40637b146eacc23b75f677c04e379d68")
}