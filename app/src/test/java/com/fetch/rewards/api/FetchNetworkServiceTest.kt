package com.fetch.rewards.api

import android.util.Log
import com.fetch.rewards.api.model.FetchItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

class FetchNetworkServiceTest {

    private val converterFactory = mockk<Converter.Factory>()
    private val retrofitBuilder = mockk<Retrofit.Builder>()
    private val retrofit = mockk<Retrofit>()
    private val fetchApi = mockk<FetchApi>()

    private val networkService = FetchNetworkService(converterFactory = converterFactory)

    init {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 1

        mockkConstructor(Retrofit.Builder::class)
        every { anyConstructed<Retrofit.Builder>().baseUrl(any<String>()) } returns retrofitBuilder

        every { retrofitBuilder.client(any()) } returns retrofitBuilder
        every { retrofitBuilder.addConverterFactory(any()) } returns retrofitBuilder
        every { retrofitBuilder.build() } returns retrofit
        every { retrofit.create(FetchApi::class.java) } returns fetchApi
    }

    @Test
    fun `getFetchItems calls API`() = runTest {
        val mockedResponse = mockk<Response<List<FetchItem>>>()
        val mockedItems = listOf(mockk<FetchItem>())

        coEvery { fetchApi.getFetchItems() } returns mockedResponse
        every { mockedResponse.isSuccessful } returns true
        every { mockedResponse.body() } returns mockedItems
        every { mockedResponse.code() } returns 200

        val response = networkService.getFetchItems()

        coVerify(exactly = 1) { fetchApi.getFetchItems() }
        Assert.assertEquals(WrappedResponse.SuccessWithBody(mockedItems), response)
    }

}