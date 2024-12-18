package com.fetch.rewards.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.fetch.rewards.model.FetchItem
import com.fetch.rewards.model.FetchList
import com.fetch.rewards.ui.theme.FetchTheme
import com.fetch.rewards.ui.theme.PaddingNormal
import com.fetch.rewards.ui.utility.LocalBottomSystemHeight
import com.fetch.rewards.viewmodel.FetchItemListViewModel

@Composable
fun FetchItemListScreen(viewModel: FetchItemListViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    FetchItemListScreen(fetchLists = viewModel.fetchLists, modifier = modifier)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FetchItemListScreen(fetchLists: List<FetchList>, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            fetchLists.forEach { fetchList ->
                stickyHeader(key = fetchList.listId) {
                    FetchSectionHeader(listId = fetchList.listId, modifier = Modifier.fillMaxWidth())
                }

                items(items = fetchList.items, key = { it.id }) { fetchItem ->
                    FetchItemRow(fetchItem = fetchItem, modifier = Modifier.fillMaxWidth())
                }
            }

            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(LocalBottomSystemHeight.current))
            }
        }
    }
}

//region Fetch Section Header

@Composable
private fun FetchSectionHeader(listId: Int, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.primary) {
        Text(
            text = listId.toString(),
            modifier = Modifier.padding(PaddingNormal),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

//endregion

//region Fetch Item Row

@Composable
private fun FetchItemRow(fetchItem: FetchItem, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Text(text = fetchItem.name, modifier = Modifier.padding(PaddingNormal))
    }
}

//endregion

@Preview(showBackground = true)
@Composable
private fun MainListScreenPreview() {
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

    FetchTheme {
        FetchItemListScreen(modifier = Modifier.fillMaxSize(), fetchLists = fetchLists)
    }
}