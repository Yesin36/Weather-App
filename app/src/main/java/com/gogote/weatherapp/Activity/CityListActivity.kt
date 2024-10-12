package com.gogote.weatherapp.Activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogote.weatherapp.Adapter.CityAdapter
import com.gogote.weatherapp.Model.CityResponseApi
import com.gogote.weatherapp.R
import com.gogote.weatherapp.ViewModel.CityViewModel
import com.gogote.weatherapp.databinding.ActivityCityListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCityListBinding
    private val cityAdapter by lazy { CityAdapter() }
    private val cityViewModel: CityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT

        }


        binding.apply {
            cityedt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    progressBar2.visibility = View.VISIBLE
                    cityViewModel.loadCity(s.toString(), 10).enqueue(object : Callback<CityResponseApi> {
                        override fun onResponse(
                            call: Call<CityResponseApi>,
                            response: Response<CityResponseApi>
                        ) {
                            if (response.isSuccessful) {
                                val data = response.body()
                                data?.let {
                                    cityAdapter.differ.submitList(it) // Assuming 'cities' is the list in CityResponseApi
                                    progressBar2.visibility = View.GONE
                                    cityView.apply {
                                        layoutManager = LinearLayoutManager(
                                            this@CityListActivity,
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                        adapter = cityAdapter
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<CityResponseApi>, t: Throwable) {
                            // Handle failure here, maybe show a toast or a retry button
                        }
                    })
                }
            })
        }
    }
}
