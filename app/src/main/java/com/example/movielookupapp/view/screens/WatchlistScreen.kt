package com.example.movielookupapp.view.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielookupapp.model.local.SortOrder
import com.example.movielookupapp.model.WatchlistEntry
import com.example.movielookupapp.model.local.WatchlistItem
import com.example.movielookupapp.model.local.toMovie
import com.example.movielookupapp.view.components.MovieCard
import com.example.movielookupapp.view.theme.MovieLookupAppTheme
import java.util.Locale

// Works with no internet: everything shown here comes from Room and DataStore (A7).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    entries: List<WatchlistEntry>,
    sortOrder: String,
    onSortChange: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onLogViewing: (Int) -> Unit,
    onRemove: (WatchlistItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("My Watchlist") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // R3: the chosen order is saved in DataStore, so it is kept after a restart.
            SortChips(
                sortOrder = sortOrder,
                onSortChange = onSortChange
            )

            if (entries.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Your watchlist is empty",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Movies you save will appear here.",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = entries,
                        key = { entry -> entry.item.movieId }
                    ) { entry ->
                        WatchlistCard(
                            entry = entry,
                            onClick = { onMovieClick(entry.item.movieId) },
                            onLogViewing = { onLogViewing(entry.item.movieId) },
                            onRemove = { onRemove(entry.item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SortChips(
    sortOrder: String,
    onSortChange: (String) -> Unit
) {
    val options = listOf(
        SortOrder.DATE_ADDED to "Date added",
        SortOrder.TITLE to "Title",
        SortOrder.RATING to "My rating"
    )

    // horizontalScroll so the chips stay reachable with very large text (R5).
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Sort:",
            style = MaterialTheme.typography.labelLarge
        )
        options.forEach { option ->
            FilterChip(
                selected = sortOrder == option.first,
                onClick = { onSortChange(option.first) },
                label = { Text(option.second) }
            )
        }
    }
}

@Composable
private fun WatchlistCard(
    entry: WatchlistEntry,
    onClick: () -> Unit,
    onLogViewing: () -> Unit,
    onRemove: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        MovieCard(
            movie = entry.item.toMovie(),
            onClick = onClick
        )

        // Worked out from this movie's viewings (WatchlistEntry.kt), the R3 relationship in use.
        Text(
            text = entry.status,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        if (entry.averageRating != null) {
            Text(
                text = "My rating " + String.format(Locale.US, "%.1f", entry.averageRating) +
                    "/5  •  watched ${entry.viewingCount}×",
                style = MaterialTheme.typography.bodySmall
            )
        }

        TextButton(
            onClick = onLogViewing,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log viewing")
        }
        TextButton(
            onClick = onRemove,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Remove", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WatchlistScreenPreview() {
    MovieLookupAppTheme {
        WatchlistScreen(
            entries = listOf(
                WatchlistEntry(
                    item = WatchlistItem(1, "Interstellar", "Space.", null, "2014", 8.7, 0L),
                    status = "Watched",
                    viewingCount = 2,
                    averageRating = 4.5,
                    lastWatched = 0L
                ),
                WatchlistEntry(
                    item = WatchlistItem(2, "Inception", "Dreams.", null, "2010", 8.4, 0L),
                    status = "Plan to watch",
                    viewingCount = 0,
                    averageRating = null,
                    lastWatched = null
                )
            ),
            sortOrder = SortOrder.DATE_ADDED,
            onSortChange = {},
            onMovieClick = {},
            onLogViewing = {},
            onRemove = {}
        )
    }
}
