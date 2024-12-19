package com.fetch.rewards.viewmodel

import com.fetch.rewards.MainDispatcherRule
import com.fetch.rewards.api.WrappedResponse
import com.fetch.rewards.api.model.FetchItem
import com.fetch.rewards.repository.FetchRepository
import com.fetch.rewards.ui.model.FetchItemRow
import com.fetch.rewards.ui.model.FetchList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class FetchItemListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchRepository = mockk<FetchRepository>()

    private val viewModel by lazy { FetchItemListViewModel(fetchRepository = fetchRepository) }

    @Test
    fun `initial state`() = runTest {
        every { fetchRepository.fetchItems } returns MutableStateFlow<List<FetchItem>?>(null)
        coEvery { fetchRepository.updateFetchItems() } returns WrappedResponse.SuccessNoBody()

        Assert.assertFalse(viewModel.isLoading)
        Assert.assertNull(viewModel.fetchLists)

        coVerify(exactly = 1) { fetchRepository.updateFetchItems() }
    }

    @Test
    fun `toFetchLists null names removed`() {
        val fetchItems = listOf(
            FetchItem(id = 0, listId = 0, name = "Item 0"),
            FetchItem(id = 1, listId = 0, name = null),
            FetchItem(id = 2, listId = 0, name = "Item 2")
        )
        val expected = listOf(
            FetchList(
                listId = 0,
                items = listOf(
                    FetchItemRow(id = 0, name = "Item 0"),
                    FetchItemRow(id = 2, name = "Item 2")
                )
            )
        )

        coEvery { fetchRepository.fetchItems } returns MutableStateFlow<List<FetchItem>?>(fetchItems)
        coEvery { fetchRepository.updateFetchItems() } returns WrappedResponse.SuccessNoBody()

        // Just give the VM some time to perform the conversion on a background thread
        viewModel.isLoading
        Thread.sleep(500L)

        val actual = viewModel.fetchLists

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `toFetchLists blank names removed`() {
        val fetchItems = listOf(
            FetchItem(id = 0, listId = 0, name = "Item 0"),
            FetchItem(id = 1, listId = 0, name = " "),
            FetchItem(id = 2, listId = 0, name = "Item 2")
        )
        val expected = listOf(
            FetchList(
                listId = 0,
                items = listOf(
                    FetchItemRow(id = 0, name = "Item 0"),
                    FetchItemRow(id = 2, name = "Item 2")
                )
            )
        )

        coEvery { fetchRepository.fetchItems } returns MutableStateFlow<List<FetchItem>?>(fetchItems)
        coEvery { fetchRepository.updateFetchItems() } returns WrappedResponse.SuccessNoBody()

        // Just give the VM some time to perform the conversion on a background thread
        viewModel.isLoading
        Thread.sleep(500L)

        val actual = viewModel.fetchLists

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `toFetchLists FetchLists are ordered`() {
        val fetchItems = listOf(
            FetchItem(id = 0, listId = 0, name = "Item 0"),
            FetchItem(id = 1, listId = 2, name = "Item 1"),
            FetchItem(id = 2, listId = 1, name = "Item 2")
        )
        val expected = listOf(
            FetchList(listId = 0, items = listOf(FetchItemRow(id = 0, name = "Item 0"))),
            FetchList(listId = 1, items = listOf(FetchItemRow(id = 2, name = "Item 2"))),
            FetchList(listId = 2, items = listOf(FetchItemRow(id = 1, name = "Item 1")))
        )

        coEvery { fetchRepository.fetchItems } returns MutableStateFlow<List<FetchItem>?>(fetchItems)
        coEvery { fetchRepository.updateFetchItems() } returns WrappedResponse.SuccessNoBody()

        // Just give the VM some time to perform the conversion on a background thread
        viewModel.isLoading
        Thread.sleep(500L)

        val actual = viewModel.fetchLists

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `toFetchLists FetchList items are ordered by name`() {
        val fetchItems = listOf(
            FetchItem(id = 0, listId = 0, name = "Item 0"),
            FetchItem(id = 1, listId = 0, name = "Item 2"),
            FetchItem(id = 2, listId = 0, name = "Item 1")
        )
        val expected = listOf(
            FetchList(
                listId = 0,
                items = listOf(
                    FetchItemRow(id = 0, name = "Item 0"),
                    FetchItemRow(id = 2, name = "Item 1"),
                    FetchItemRow(id = 1, name = "Item 2")
                )
            )
        )

        coEvery { fetchRepository.fetchItems } returns MutableStateFlow<List<FetchItem>?>(fetchItems)
        coEvery { fetchRepository.updateFetchItems() } returns WrappedResponse.SuccessNoBody()

        // Just give the VM some time to perform the conversion on a background thread
        viewModel.isLoading
        Thread.sleep(500L)

        val actual = viewModel.fetchLists

        Assert.assertEquals(expected, actual)
    }

}