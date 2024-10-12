package com.gogote.weatherapp.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.github.matteobattilana.weather.PrecipType
import com.gogote.weatherapp.Adapter.ForecastAdapter
import com.gogote.weatherapp.Model.CurrentResponseApi
import com.gogote.weatherapp.Model.ForcastResponseApi
import com.gogote.weatherapp.R
import com.gogote.weatherapp.ViewModel.WeatherViewModel
import com.gogote.weatherapp.databinding.ActivityMainBinding
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    private val forcastAdapter by lazy { ForecastAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT

        }


        binding.apply {
            var lat = intent.getDoubleExtra("lat", 0.0)
            var lon = intent.getDoubleExtra("lon", 0.0)
            var name = intent.getStringExtra("name")

            if(lat == 0.0){
             lat = 51.50
             lon= -0.12
             name = "Rangpur"
            }

            add.setOnClickListener {
                startActivity(Intent(this@MainActivity, CityListActivity::class.java))
            }

            // current temp

            cityTxt.text = name
            progressBar.visibility = View.VISIBLE

            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            statusTxt.text = it.weather?.get(0)?.main ?: "-"
                            windTxt.text = "${it.wind.speed.toInt()} Km"
                            currentTemTxt.text = "${it.main.temp.toInt()}°"
                            maxTemTxt.text = "${it.main.tempMax.toInt()}°"
                            minTemTxt.text = "${it.main.tempMin.toInt()}°"
                            humidityTxt.text = "${it.main.humidity.toInt()}%"


                            val drawable = if (isNightNow()) R.drawable.night_bg
                            else{
//                                it.weather?.get(0)?.icon?.let { it1 -> setDynanamicallyWallpaper(it1) }
                                setDynanamicallyWallpaper(it.weather?.get(0)?.icon?:"-")

                            }
                            bgimage.setImageResource(drawable)
                            setEffectRainSnow(it.weather?.get(0)?.icon?:"-")


                        }


                    }

                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }

            })

            // setting Blue view

            var radius = 10f
            val decorView = window.decorView
            val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
            val windowBackground = decorView.background

            rootView?.let {
                bluerview.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                bluerview.outlineProvider = ViewOutlineProvider.BACKGROUND
                bluerview.clipToOutline = true
            }

            //forcast temp
            weatherViewModel.loadForecastWeather(lat, lon, "metric").enqueue(object : retrofit2.Callback<ForcastResponseApi> {
                override fun onResponse(
                    call: Call<ForcastResponseApi>,
                    response: Response<ForcastResponseApi>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        bluerview.visibility = View.VISIBLE
                        data?.let {
                            forcastAdapter.differ.submitList(it.forecastList) // Changed to 'forecastList'
                            forcastview.apply {
                                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false) // Fixed LayoutManager initialization
                                adapter = forcastAdapter
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ForcastResponseApi>, t: Throwable) {

                }

                })


        }
    }

    private fun isNightNow(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setDynanamicallyWallpaper(icon: String): Int {
        // Determine the wallpaper based on the weather icon
        return when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg  // Use the correct background drawable
            }
            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }
            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg
            }
            "13" -> {
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg
            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }
            else -> {
                // Return a default drawable or handle the case where no valid icon is available
                0
            }
        }
    }

    private fun setEffectRainSnow(icon: String) {
        // Determine the wallpaper based on the weather icon
         when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)

            }
            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)

            }
            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)

            }
            "13" -> {
                initWeatherView(PrecipType.SNOW)

            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)

            }

        }
    }

    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
//            speed = 10000
            angle = 20
            emissionRate = 100.0f
        }
    }
}


