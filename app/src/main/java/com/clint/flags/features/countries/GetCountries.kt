package com.clint.flags.features.countries

import com.clint.flags.core.exception.Failure
import com.clint.flags.core.functional.Either
import com.clint.flags.core.interactor.UseCase
import javax.inject.Inject

class GetCountries @Inject constructor(private val countriesRepository: CountriesRepository) :
    UseCase<List<Countries>, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, List<Countries>> {
        return countriesRepository.countries()
    }

}