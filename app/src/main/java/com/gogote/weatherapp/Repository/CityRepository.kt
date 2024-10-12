package com.gogote.weatherapp.Repository

import com.gogote.weatherapp.Server.ApiServices

class CityRepository (val apiServices: ApiServices){

    fun getCities(q: String, limit: Int) =
        apiServices.getCityList(q, limit, "40637b146eacc23b75f677c04e379d68")
}