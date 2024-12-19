package com.fetch.rewards.repository

import android.util.Log
import com.fetch.rewards.api.FetchNetworkService
import com.fetch.rewards.api.WrappedResponse
import com.fetch.rewards.api.model.FetchItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FetchRepositoryTest {

    private val networkService = mockk<FetchNetworkService>()

    private val repository = FetchRepository(networkService = networkService)
    init {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 1
        every { Log.e(any(), any(), any()) } returns 1
    }

    @Test
    fun `initial state test`() {
        Assert.assertNull(repository.fetchItems.value)
    }

    @Test
    fun `updateFetchItems success`() = runTest {
        val fetchItems = listOf(mockk<FetchItem>())
        val expected = WrappedResponse.SuccessWithBody(fetchItems)

        coEvery { networkService.getFetchItems() } returns expected

        val result = repository.updateFetchItems()

        Assert.assertEquals(result, expected)
        Assert.assertEquals(fetchItems, repository.fetchItems.value)
    }

    @Test
    fun `updateFetchItems success no body`() = runTest {
        val expected = WrappedResponse.SuccessNoBody<List<FetchItem>>()

        coEvery { networkService.getFetchItems() } returns expected

        val result = repository.updateFetchItems()

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `updateFetchItems network error`() = runTest {
        val expected = WrappedResponse.NetworkError<List<FetchItem>>(mockk(), mockk(), 404)

        coEvery { networkService.getFetchItems() } returns expected

        val result = repository.updateFetchItems()

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `updateFetchItems local error`() = runTest {
        val expected = WrappedResponse.LocalError<List<FetchItem>>(mockk())

        coEvery { networkService.getFetchItems() } returns expected

        val result = repository.updateFetchItems()

        Assert.assertEquals(expected, result)
    }

}