package com.clint.flags.features.countries

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.clint.flags.R
import com.clint.flags.core.Constants.GRID_SPAN_COUNT
import com.clint.flags.core.Constants.SPLASH_SCREEN_DELAY
import com.clint.flags.core.Constants.VEILED_ITEMS
import com.clint.flags.core.exception.Failure
import com.clint.flags.core.extensions.failure
import com.clint.flags.core.extensions.observe
import com.clint.flags.core.extensions.showToast
import com.clint.flags.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountriesActivity : AppCompatActivity() {
    @Inject
    lateinit var countriesAdapter: CountriesAdapter
    private lateinit var binding: ActivityMainBinding
    private var keepSplashOnScreen = true
    private val countriesViewModel: CountriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseView()
        Handler(Looper.getMainLooper()).postDelayed(
            { keepSplashOnScreen = false }, SPLASH_SCREEN_DELAY
        )
        with(countriesViewModel) {
            observe(countries, ::renderCountries)
            failure(failure, ::renderFailure)
        }
    }

    private fun renderFailure(failure: Failure?) {
        when (failure) {
            Failure.NetworkConnection -> {
                showToast(getString(R.string.network_error))
            }
            Failure.ServerError -> {
                showToast(getString(R.string.server_error))
            }
            else -> {
                showToast(getString(R.string.some_error))
            }
        }
        binding.apply {
            lottieError.visibility = View.VISIBLE
            recyclerViewCountries.visibility = View.GONE
        }
    }

    private fun renderCountries(countries: List<Countries>?) {
        countriesAdapter.countriesList = countries.orEmpty()
        binding.apply {
            recyclerViewCountries.visibility = View.VISIBLE
            lottieError.visibility = View.GONE
            recyclerViewCountries.unVeil()
        }
    }

    private fun initialiseView() {
        binding.recyclerViewCountries.run {
            setVeilLayout(R.layout.row_flags)
            setAdapter(countriesAdapter)
            setLayoutManager(GridLayoutManager(this@CountriesActivity, GRID_SPAN_COUNT))
            addVeiledItems(VEILED_ITEMS)
        }
    }
}