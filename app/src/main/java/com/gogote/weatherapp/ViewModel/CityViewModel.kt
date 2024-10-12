package com.gogote.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import com.gogote.weatherapp.Repository.CityRepository
import com.gogote.weatherapp.Server.ApiClient
import com.gogote.weatherapp.Server.ApiServices

class CityViewModel(val repository: CityRepository) : ViewModel() {

    constructor() : this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCity(q: String, limit: Int) = repository.getCities(q, limit)
}