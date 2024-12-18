package com.fetch.rewards.repository

import android.util.Log
import com.fetch.rewards.api.FetchNetworkService
import com.fetch.rewards.api.WrappedResponse
import com.fetch.rewards.api.model.FetchItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FetchRepo"

@Singleton
class FetchRepository @Inject constructor(private val networkService: FetchNetworkService) {

    private var _fetchItems = MutableStateFlow(emptyList<FetchItem>())
    val fetchItems: StateFlow<List<FetchItem>> get() = _fetchItems

    suspend fun updateFetchItems() = networkService.getFetchItems().also { response ->
        response.requireResponseBody(
            onSuccess = { _fetchItems.value = it },
            onFailureLogMessage = "Error getting fetch items"
        )
    }

    //TODO Move to base class
    private fun <T> WrappedResponse<T>.requireResponseBody(onSuccess: (T) -> Unit, onFailureLogMessage: String) {
        when(this) {
            is WrappedResponse.SuccessWithBody -> onSuccess(this.body)
            is WrappedResponse.SuccessNoBody -> Log.e(TAG, "$onFailureLogMessage: No Response Body")
            is WrappedResponse.NetworkError -> {
                Log.e(TAG, "$onFailureLogMessage: Network Error ${this.responseCode}", this.throwable)
            }
            is WrappedResponse.LocalError -> Log.e(TAG, "$onFailureLogMessage: Local Error", this.throwable)
        }
    }

}