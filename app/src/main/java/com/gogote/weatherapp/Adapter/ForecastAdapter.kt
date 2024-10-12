package com.gogote.weatherapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gogote.weatherapp.Model.ForcastResponseApi
import com.gogote.weatherapp.databinding.ForecastViewholderBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    private lateinit var binding: ForecastViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ForecastViewholderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastAdapter.ViewHolder, position: Int) {
        val forecastData = differ.currentList[position]

        // Parse the date and time
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(forecastData.dtTxt)
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Set the day of the week
        val dayOfWeekName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "-"
        }
        holder.binding.dayname.text = dayOfWeekName

        // Set the time (AM/PM)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val ampm = if (hour < 12) "AM" else "PM"
        val hour12 = calendar.get(Calendar.HOUR)
        holder.binding.hourtex.text = "$hour12 $ampm"

        // Set the temperature
        holder.binding.temtext.text = forecastData.main.temp.let { Math.round(it) }.toString() + "°"

        // Map the icon
        val icon = when (forecastData.weather[0].icon) {
            "01d", "0n" -> "sunny"
            "02d", "02n" -> "cloudy_sunny"
            "03d", "03n" -> "cloudy_sunny"
            "04d", "04n" -> "cloudy"
            "09d", "09n" -> "rainy"
            "10d", "10n" -> "rainy"
            "11d", "11n" -> "storm"
            "13d", "13n" -> "snowy"
            "50d", "50n" -> "windy"
            else -> "sunny"
        }

        // Set the weather icon using Glide
        val drawableResID: Int = holder.binding.root.context.resources.getIdentifier(
            icon, "drawable", holder.binding.root.context.packageName
        )
        Glide.with(holder.binding.root.context)
            .load(drawableResID)
            .into(holder.binding.pic)
    }

    inner class ViewHolder(val binding: ForecastViewholderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = differ.currentList.size

    // DiffUtil Callback
    private val differCallback = object : DiffUtil.ItemCallback<ForcastResponseApi.ForecastData>() {
        override fun areItemsTheSame(oldItem: ForcastResponseApi.ForecastData, newItem: ForcastResponseApi.ForecastData): Boolean {
            return oldItem.dtTxt == newItem.dtTxt // Unique identifier for comparison
        }

        override fun areContentsTheSame(oldItem: ForcastResponseApi.ForecastData, newItem: ForcastResponseApi.ForecastData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
