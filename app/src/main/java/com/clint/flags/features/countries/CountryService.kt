package com.clint.flags.features.countries

import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject

class CountryService @Inject constructor(private val retrofit: Retrofit) : CountriesApi {

    private val countriesApi by lazy { retrofit.create(CountriesApi::class.java) }

    override fun getCountries(): Call<List<Countries>> {
        return countriesApi.getCountries()
    }

}