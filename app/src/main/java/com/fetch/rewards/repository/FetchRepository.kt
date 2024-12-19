package com.fetch.rewards.repository

import com.fetch.rewards.api.FetchNetworkService
import com.fetch.rewards.api.model.FetchItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchRepository @Inject constructor(private val networkService: FetchNetworkService) : Repository() {

    //TODO Default to null instead of empty list to account for no items returned from API
    private var _fetchItems = MutableStateFlow(emptyList<FetchItem>())
    val fetchItems: StateFlow<List<FetchItem>> get() = _fetchItems

    suspend fun updateFetchItems() = networkService.getFetchItems().also { response ->
        response.requireResponseBody(
            onSuccess = { _fetchItems.value = it },
            onFailureLogMessage = "Error getting fetch items"
        )
    }

}