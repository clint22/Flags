package com.clint.flags.features.countries

import retrofit2.Call
import retrofit2.http.GET

interface CountriesApi {
    @GET("ae2e72187d5e63989a32")
    fun getCountries(): Call<List<Countries>>
}