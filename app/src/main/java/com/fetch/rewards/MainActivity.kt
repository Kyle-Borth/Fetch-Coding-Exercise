package com.fetch.rewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.fetch.rewards.ui.screen.FetchItemListScreen
import com.fetch.rewards.ui.theme.FetchTheme
import com.fetch.rewards.ui.utility.LocalBottomSystemHeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CompositionLocalProvider(
                        LocalBottomSystemHeight provides innerPadding.calculateBottomPadding()
                    ) {
                        FetchItemListScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
                    }
                }
            }
        }
    }
}
