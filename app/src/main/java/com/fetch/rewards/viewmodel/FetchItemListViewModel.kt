package com.fetch.rewards.viewmodel

import androidx.lifecycle.ViewModel
import com.fetch.rewards.model.FetchItem
import com.fetch.rewards.model.FetchList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FetchItemListViewModel @Inject constructor() : ViewModel() {

    val fetchLists = mutableListOf<FetchList>().apply {
        for(listIndex in 1..10) {
            val fetchItems = mutableListOf<FetchItem>().apply {
                for(itemIndex in 1..3) {
                    val itemId = (listIndex * 10) + itemIndex
                    add(FetchItem(id = itemId, listId = listIndex, name = "List Item $itemId"))
                }
            }.toList()

            add(FetchList(listId = listIndex, items = fetchItems))
        }
    }.toList()

}