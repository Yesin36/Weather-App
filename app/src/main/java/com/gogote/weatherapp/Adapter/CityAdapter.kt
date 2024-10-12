package com.gogote.weatherapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gogote.weatherapp.Activity.MainActivity
import com.gogote.weatherapp.Model.CityResponseApi
import com.gogote.weatherapp.Model.ForcastResponseApi
import com.gogote.weatherapp.databinding.CityviewholderBinding
import com.gogote.weatherapp.databinding.ForecastViewholderBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    private lateinit var binding: CityviewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = CityviewholderBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
        val binding = CityviewholderBinding.bind(holder.itemView)
        binding.citytxt.text = differ.currentList[position].name
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, MainActivity::class.java)
            intent.putExtra("lat", differ.currentList[position].lat)
            intent.putExtra("lon", differ.currentList[position].lon)
            intent.putExtra("name", differ.currentList[position].name)
            binding.root.context.startActivity(intent)
        }
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)



private val differCallback = object : DiffUtil.ItemCallback<CityResponseApi.CityResponseApiItem>() {
    override fun areItemsTheSame(
        oldItem: CityResponseApi.CityResponseApiItem,
        newItem: CityResponseApi.CityResponseApiItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: CityResponseApi.CityResponseApiItem,
        newItem: CityResponseApi.CityResponseApiItem
    ): Boolean {
        return oldItem == newItem
    }

}

    val differ = AsyncListDiffer(this, differCallback)
}
