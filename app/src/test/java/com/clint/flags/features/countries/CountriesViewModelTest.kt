package com.clint.flags.features.countries

import com.clint.flags.AndroidTest
import com.clint.flags.core.functional.Either
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class CountriesViewModelTest : AndroidTest() {
    private lateinit var countriesViewModel: CountriesViewModel

    @MockK
    private lateinit var getCountries: GetCountries

    @Before
    fun setup() {
        countriesViewModel = CountriesViewModel(getCountries)
    }

    @Test
    fun `loading countries should update livedata`() {
        val picturesList = listOf(
            Countries(name = "India", capital = "NewDelhi"),
            Countries(name = "Thailand", capital = "Bangkok")
        )
        coEvery { getCountries.run(any()) } returns Either.Right(picturesList)
        countriesViewModel.countries.observeForever {
            it.apply {
                size shouldBeEqualTo 2
                it[0].name shouldBeEqualTo "India"
                it[0].capital shouldBeEqualTo "NewDelhi"
                it[1].name shouldBeEqualTo "Thailand"
                it[1].capital shouldBeEqualTo "Bangkok"
            }
        }
        runBlocking { countriesViewModel.loadCountries() }
    }
}