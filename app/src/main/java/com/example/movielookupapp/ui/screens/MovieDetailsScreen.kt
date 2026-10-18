package com.example.movielookupapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielookupapp.ui.components.MoviePosterPlaceholder
import com.example.movielookupapp.ui.theme.MovieLookupAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    onLogViewingClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Fake data for the UI assignment stage.
    val title = "Interstellar"
    val releaseYear = "2014"
    val rating = 8.7
    val overview =
        "A team of explorers travels beyond this galaxy to discover whether mankind has a future among the stars."

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MoviePosterPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "$releaseYear  •  ★ ${"%.1f".format(rating)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = overview,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Watchlist")
            }

            OutlinedButton(
                onClick = { onLogViewingClick(movieId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Viewing")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenPreview() {
    MovieLookupAppTheme {
        MovieDetailsScreen(movieId = 1)
    }
}