package com.fetch.rewards.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.fetch.rewards.R
import com.fetch.rewards.ui.component.NetworkError
import com.fetch.rewards.ui.model.FetchItemRow
import com.fetch.rewards.ui.model.FetchList
import com.fetch.rewards.ui.theme.FetchTheme
import com.fetch.rewards.ui.theme.PaddingLarge
import com.fetch.rewards.ui.theme.PaddingNormal
import com.fetch.rewards.ui.theme.PaddingSmall
import com.fetch.rewards.ui.utility.LocalBottomSystemHeight
import com.fetch.rewards.viewmodel.FetchItemListViewModel

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
    fetchLists: List<FetchList>?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onUpdateList: () -> Unit
) {
    Surface(modifier = modifier) {
        when {
            !fetchLists.isNullOrEmpty() -> {
                FetchItemList(
                    fetchLists = fetchLists,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = onUpdateList
                )
            }
            isLoading -> {
                Loading(modifier = Modifier.fillMaxSize())
            }
            fetchLists == null -> {
                NetworkError(modifier = Modifier.fillMaxSize().padding(PaddingNormal), onRetry = onUpdateList)
            }
            else -> {
                NoItems(modifier = Modifier.fillMaxSize().padding(PaddingNormal), onRetry = onUpdateList)
            }
        }
    }
}

//region Fetch Item List

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun FetchItemList(
    fetchLists: List<FetchList>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(isRefreshing = isLoading, onRefresh = onRefresh, modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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

//region Loading

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

//endregion

//region No Items

@Composable
private fun NoItems(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_items),
            modifier = Modifier.padding(bottom = PaddingSmall),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = stringResource(R.string.try_later),
            modifier = Modifier.padding(bottom = PaddingLarge),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedButton(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
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