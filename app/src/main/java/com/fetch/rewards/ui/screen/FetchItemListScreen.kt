package com.fetch.rewards.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.fetch.rewards.ui.component.NetworkError
import com.fetch.rewards.ui.model.FetchItemRow
import com.fetch.rewards.ui.model.FetchList
import com.fetch.rewards.ui.theme.FetchTheme
import com.fetch.rewards.ui.theme.PaddingNormal
import com.fetch.rewards.ui.utility.LocalBottomSystemHeight
import com.fetch.rewards.viewmodel.FetchItemListViewModel

//TODO Implement Pull-To-Refresh

@Composable
fun FetchItemListScreen(viewModel: FetchItemListViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    FetchItemListScreen(
        fetchLists = viewModel.fetchLists,
        isLoading = viewModel.isLoading,
        modifier = modifier,
        onUpdateList = { viewModel.updateFetchItems() }
    )
}

@Composable
private fun FetchItemListScreen(
    fetchLists: List<FetchList>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onUpdateList: () -> Unit
) {
    Surface(modifier = modifier) {
        if(isLoading || fetchLists.isNotEmpty()) {
            FetchItemList(fetchLists = fetchLists, isLoading = isLoading, modifier = Modifier.fillMaxSize())
        }
        else {
            NetworkError(modifier = Modifier.fillMaxSize().padding(PaddingNormal), onRetry = onUpdateList)
        }
    }
}

//region Fetch Item List

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FetchItemList(fetchLists: List<FetchList>, isLoading: Boolean, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        if(isLoading) {
            item {
                LoadingRow(modifier = Modifier.fillMaxWidth())
            }
        }

        fetchLists.forEach { fetchList ->
            stickyHeader(key = fetchList.listId) {
                FetchSectionHeader(listId = fetchList.listId, modifier = Modifier.fillMaxWidth())
            }

            items(items = fetchList.items, key = { it.id }) { fetchItemRow ->
                FetchItemRow(fetchItemRow = fetchItemRow, modifier = Modifier.fillMaxWidth())
            }
        }

        item {
            Spacer(modifier = Modifier.fillMaxWidth().height(LocalBottomSystemHeight.current))
        }
    }
}

//endregion

//region Loading Row

@Composable
private fun LoadingRow(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

//endregion

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
private fun FetchItemRow(fetchItemRow: FetchItemRow, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Text(text = fetchItemRow.name, modifier = Modifier.padding(PaddingNormal))
    }
}

//endregion

@Preview(showBackground = true)
@Composable
private fun MainListScreenPreview() {
    val fetchLists = listOf(
        FetchList(
            listId = 1,
            items = listOf(
                FetchItemRow(id = 101, name = "Item 101"),
                FetchItemRow(id = 102, name = "Item 102"),
                FetchItemRow(id = 103, name = "Item 103")
            )
        ),
        FetchList(
            listId = 2,
            items = listOf(
                FetchItemRow(id = 201, name = "Item 201"),
                FetchItemRow(id = 202, name = "Item 202"),
                FetchItemRow(id = 203, name = "Item 203")
            )
        )
    )

    FetchTheme {
        FetchItemListScreen(
            modifier = Modifier.fillMaxSize(),
            isLoading = false,
            fetchLists = fetchLists,
            onUpdateList = {}
        )
    }
}