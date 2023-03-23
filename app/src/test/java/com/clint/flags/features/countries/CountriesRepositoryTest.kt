package com.clint.flags.features.countries

import com.clint.flags.UnitTest
import com.clint.flags.core.exception.Failure
import com.clint.flags.core.functional.Either
import com.clint.flags.core.platform.NetworkHandler
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class CountriesRepositoryTest : UnitTest() {

    private lateinit var networkRepository: Network

    @MockK
    private lateinit var networkHandler: NetworkHandler

    @MockK
    private lateinit var countryService: CountryService

    @MockK
    private lateinit var countryResponse: Response<List<Countries>>

    @MockK
    private lateinit var countriesCall: Call<List<Countries>>

    @Before
    fun setup() {
        networkRepository = Network(networkHandler, countryService)
    }

    @Test
    fun `should return empty list by default`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { countryResponse.body() } returns null
        every { countryResponse.isSuccessful } returns true
        every { countriesCall.execute() } returns countryResponse
        every { countryService.getCountries() } returns countriesCall
        val pictures = networkRepository.countries()
        pictures shouldBeEqualTo Either.Right(emptyList())
        verify(exactly = 1) { countryService.getCountries() }
    }

    @Test
    fun `should get countries list from service`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { countryResponse.body() } returns listOf(
            Countries(
                name = "India",
                capital = "NewDelhi"
            )
        )
        every { countryResponse.isSuccessful } returns true
        every { countriesCall.execute() } returns countryResponse
        every { countryService.getCountries() } returns countriesCall
        val pictures = networkRepository.countries()
        pictures shouldBeEqualTo Either.Right(
            listOf(
                Countries(
                    name = "India",
                    capital = "NewDelhi"
                )
            )
        )
        verify(exactly = 1) { countryService.getCountries() }
    }

    @Test
    fun `country service should return network failure when no connection`() {
        every { networkHandler.isNetworkAvailable() } returns false
        val pictures = networkRepository.countries()
        pictures shouldBeInstanceOf Either::class.java
        pictures.isLeft shouldBeEqualTo true
        pictures.fold(
            { failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java },
            {})
        verify { countryService wasNot called }
    }

    @Test
    fun `country service should return server error when response is not successful`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { countryResponse.isSuccessful } returns false
        every { countriesCall.execute() } returns countryResponse
        every { countryService.getCountries() } returns countriesCall
        val pictures = networkRepository.countries()
        pictures shouldBeInstanceOf Either::class.java
        pictures.isLeft shouldBeEqualTo true
        pictures.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    @Test
    fun `country request should capture exceptions`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { countriesCall.execute() } returns countryResponse
        every { countryService.getCountries() } returns countriesCall
        val pictures = networkRepository.countries()
        pictures shouldBeInstanceOf Either::class.java
        pictures.isLeft shouldBeEqualTo true
        pictures.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }
}