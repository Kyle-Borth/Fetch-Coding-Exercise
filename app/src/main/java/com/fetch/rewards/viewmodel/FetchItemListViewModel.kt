package com.fetch.rewards.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fetch.rewards.api.model.FetchItem
import com.fetch.rewards.repository.FetchRepository
import com.fetch.rewards.ui.model.FetchItemRow
import com.fetch.rewards.ui.model.FetchList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FetchItemListViewModel @Inject constructor(private val fetchRepository: FetchRepository) : ViewModel() {

    private var fetchingItems by mutableStateOf(true)
    private var groupingItems by mutableStateOf(false)
    val isLoading by derivedStateOf { fetchingItems || groupingItems }

    var fetchLists by mutableStateOf(emptyList<FetchList>())
        private set

    init {
        viewModelScope.launch {
            fetchRepository.fetchItems.collectLatest { fetchItems ->
                groupingItems = true

                fetchLists = fetchItems.toFetchLists()

                groupingItems = false
            }
        }

        updateFetchItems()
    }

    private fun updateFetchItems() = viewModelScope.launch {
        fetchingItems = true

        fetchRepository.updateFetchItems()

        fetchingItems = false
    }

    /**
     * Converts a list of [FetchItem]s into a sorted list of [FetchList]s, grouping them by their listIds.
     * Also filters out any [FetchItem]s with a null or blank name.
     */
    private suspend fun List<FetchItem>.toFetchLists() = withContext(Dispatchers.Default) {
        filter { !it.name.isNullOrBlank() }
            .groupBy { it.listId }
            .toSortedMap()
            .map { map ->
                val rowItems = map.value.map { fetchItem ->
                    FetchItemRow(id = fetchItem.id, name = fetchItem.name!!)
                }.sortedWith(compareBy({ it.name }, { it.id }))

                FetchList(listId = map.key, items = rowItems)
            }
    }

}