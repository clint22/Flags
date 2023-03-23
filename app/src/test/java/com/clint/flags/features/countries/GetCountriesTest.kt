package com.clint.flags.features.countries

import com.clint.flags.UnitTest
import com.clint.flags.core.functional.Either
import com.clint.flags.core.interactor.UseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCountriesTest : UnitTest() {
    private lateinit var getCountries: GetCountries

    @MockK
    private lateinit var countriesRepository: CountriesRepository

    @Before
    fun setup() {
        getCountries = GetCountries(countriesRepository)
        every { countriesRepository.countries() } returns Either.Right(listOf(Countries.empty))
    }

    @Test
    fun `should get data from the repository`() {
        runBlocking { getCountries.run(UseCase.None()) }
        verify(exactly = 1) { countriesRepository.countries() }
    }
}