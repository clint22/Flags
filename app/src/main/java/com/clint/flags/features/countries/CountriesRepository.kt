package com.clint.flags.features.countries

import com.clint.flags.core.exception.Failure
import com.clint.flags.core.functional.Either
import com.clint.flags.core.functional.Request.request
import com.clint.flags.core.platform.NetworkHandler
import javax.inject.Inject

interface CountriesRepository {
    fun countries(): Either<Failure, List<Countries>>
}

class Network @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val countryService: CountryService
) :
    CountriesRepository {
    override fun countries(): Either<Failure, List<Countries>> {
        return when (networkHandler.isNetworkAvailable()) {
            true -> {
                request(countryService.getCountries(), {
                    it
                }, mutableListOf())
            }
            false -> {
                Either.Left(Failure.NetworkConnection)
            }
        }
    }

}