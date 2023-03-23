package com.clint.flags.features.countries

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.clint.flags.R
import com.clint.flags.core.Constants.INTENT_KEY_COUNTRY_EXTRA_NAME
import com.clint.flags.core.Constants.INTENT_KEY_COUNTRY_EXTRA_POSITION_NAME
import com.clint.flags.core.Constants.ITEM_TRANSITION_MIN_SCALE
import com.clint.flags.core.Constants.ITEM_TRANSITION_TIME_IN_MILLIS
import com.clint.flags.core.exception.Failure
import com.clint.flags.core.extensions.*
import com.clint.flags.core.view.discreteScrollView.DSVOrientation
import com.clint.flags.core.view.discreteScrollView.DiscreteScrollView
import com.clint.flags.core.view.discreteScrollView.InfiniteScrollAdapter
import com.clint.flags.core.view.discreteScrollView.transform.ScaleTransformer
import com.clint.flags.databinding.ActivityCountryDetailBinding
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountryDetailActivity : AppCompatActivity(),
    DiscreteScrollView.OnItemChangedListener<CountriesAdapterScroll.ViewHolder> {
    @Inject
    lateinit var countriesAdapterScroll: CountriesAdapterScroll
    private lateinit var infiniteAdapter: InfiniteScrollAdapter<*>
    private val countriesViewModel: CountriesViewModel by viewModels()
    private var countries: List<Countries>? = null
    private var country: Countries? = null
    private var loadedFirstTime = true
    private var picturePosition: Int = 0
    private lateinit var binding: ActivityCountryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            this.setTheme(R.style.Theme_Countries)
        }
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            installSplashScreen()
        }
        binding = ActivityCountryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseView()
//        loadCountries()
        setClickListeners()
        with(countriesViewModel) {
            observe(countries, ::renderCountries)
            failure(failure, ::renderFailure)
        }
    }

    private fun setClickListeners() {
        binding.imageViewBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun renderFailure(failure: Failure?) {
        binding.apply {
            progressDiscreetScroll.visibility = View.GONE
            textViewRelatedError.visibility = View.VISIBLE
        }
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
    }

    private fun renderCountries(countries: List<Countries>?) {
        this.countries = countries
        countriesAdapterScroll.countriesList = countries.orEmpty()
        binding.run {
            picturesScrollView.setOrientation(DSVOrientation.HORIZONTAL)
            picturesScrollView.addOnItemChangedListener(this@CountryDetailActivity)
            infiniteAdapter = InfiniteScrollAdapter.wrap(countriesAdapterScroll)
            picturesScrollView.adapter = infiniteAdapter
            picturesScrollView.setItemTransitionTimeMillis(ITEM_TRANSITION_TIME_IN_MILLIS)
            picturesScrollView.setItemTransformer(
                ScaleTransformer.Builder().setMinScale(ITEM_TRANSITION_MIN_SCALE).build()
            )
            picturesScrollView.scrollToPosition(picturePosition)
            progressDiscreetScroll.visibility = View.GONE
            picturesScrollView.visibility = View.VISIBLE
        }
    }

    private fun loadCountries() {
        countriesViewModel.loadCountries()
    }

    private fun initialiseView() {
        intent.getIntExtra(pictureExtraPositionName, 0).let {
            picturePosition = it
        }
        intent.parcelable<Countries>(pictureExtraName)?.let {
            country = it
            country?.apply {
                setupDetailView(
                    currency,
                    population,
                    symbol,
                    capital,
                    region,
                    subregion,
                    flag,
                    name
                )
            }
        }
    }

    private fun setupDetailView(
        currency: String?,
        population: String?,
        symbol: String?,
        capital: String?,
        region: String?,
        subregion: String?,
        flag: String?,
        title: String?
    ) {
        binding.run {
            textViewCurrency.text = getString(
                R.string.currency,
                currency
            )
            textViewPopulation.text = getString(R.string.population, population)
            textViewSymbol.text = getString(R.string.currency_symbol, symbol)
            textViewCapital.text = getString(R.string.capital, capital)
            textViewRegion.text = getString(R.string.region, region)
            textViewSubRegion.text = getString(R.string.sub_region, subregion)
            detailTitleTextView.text = title
            detailImageView.loadFromUrl(flag)
        }
    }


    override fun onCurrentItemChanged(
        viewHolder: CountriesAdapterScroll.ViewHolder?,
        adapterPosition: Int
    ) {
        if (!loadedFirstTime) {
            val positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition)
            country = countries?.get(positionInDataSet)
            country?.apply {
                setupDetailView(
                    currency, population, symbol, capital, region, subregion, flag, name
                )
            }
        }
        loadedFirstTime = false
    }

    companion object {
        private const val pictureExtraName = INTENT_KEY_COUNTRY_EXTRA_NAME
        private const val pictureExtraPositionName = INTENT_KEY_COUNTRY_EXTRA_POSITION_NAME
        fun startActivity(
            context: Context,
            transformationLayout: TransformationLayout,
            pictures: Countries,
            position: Int
        ) {
            val intent = Intent(context, CountryDetailActivity::class.java)
            intent.putExtra(pictureExtraName, pictures)
            intent.putExtra(pictureExtraPositionName, position)
            TransformationCompat.startActivity(transformationLayout, intent)

        }
    }
}