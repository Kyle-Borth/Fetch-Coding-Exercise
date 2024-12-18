package com.fetch.rewards.ui.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fetch.rewards.model.FetchItem
import com.fetch.rewards.ui.theme.FetchTheme
import com.fetch.rewards.ui.theme.PaddingNormal
import com.fetch.rewards.ui.utility.LocalBottomSystemHeight

@Composable
fun MainListScreen(modifier: Modifier = Modifier) {
    //TODO
}

@Composable
private fun MainListScreen(modifier: Modifier = Modifier, fetchItems: List<FetchItem>) {
    //val groupedItems = fetchItems.groupBy { it.listId }.toSortedMap()


    Surface(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {


            items(items = fetchItems, key = { it.id }, contentType = { it.listId }) { fetchItem ->
                FetchItemRow(
                    fetchItem = fetchItem,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = PaddingNormal)
                )
            }

            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(LocalBottomSystemHeight.current))
            }
        }
    }
}

//region Fetch Item Row

@Composable
private fun FetchItemRow(fetchItem: FetchItem, modifier: Modifier = Modifier) {

}

//endregion

@Preview(showBackground = true)
@Composable
private fun MainListScreenPreview() {
    val fetchItems = mutableListOf<FetchItem>().apply {
        for(i in 0..10) {
            add(FetchItem(id = i, listId = i % 2, name = "List Item $i"))
        }
    }.toList()

    FetchTheme {
        MainListScreen(modifier = Modifier.fillMaxSize(), fetchItems = fetchItems)
    }
}