package com.fetch.rewards.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fetch.rewards.R
import com.fetch.rewards.ui.theme.FetchTheme
import com.fetch.rewards.ui.theme.PaddingLarge
import com.fetch.rewards.ui.theme.PaddingNormal
import com.fetch.rewards.ui.theme.PaddingSmall

@Composable
fun NetworkError(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.network_error),
            modifier = Modifier.padding(bottom = PaddingSmall),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = stringResource(R.string.try_again),
            modifier = Modifier.padding(bottom = PaddingLarge),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedButton(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkErrorPreview() {
    FetchTheme {
        Surface {
            NetworkError(modifier = Modifier.padding(PaddingNormal), onRetry = {})
        }
    }
}