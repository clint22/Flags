package com.clint.flags.features.countries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.clint.flags.core.interactor.UseCase
import com.clint.flags.core.platform.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountries: GetCountries
) : BaseViewModel() {

    init {
        loadCountries()
    }

    private val _countries: MutableLiveData<List<Countries>> = MutableLiveData()
    val countries = _countries

    fun loadCountries() = getCountries(UseCase.None(), viewModelScope) {
        it.fold(::handleFailure, ::handleCountries)
    }

    private fun handleCountries(countries: List<Countries>) {
        _countries.value = countries
    }
}