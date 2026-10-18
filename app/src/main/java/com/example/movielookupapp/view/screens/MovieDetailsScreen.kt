package com.example.movielookupapp.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielookupapp.model.Movie
import com.example.movielookupapp.model.local.Viewing
import com.example.movielookupapp.model.formatDate
import com.example.movielookupapp.model.posterUrl
import com.example.movielookupapp.view.components.ErrorView
import com.example.movielookupapp.view.components.LoadingView
import com.example.movielookupapp.view.components.MoviePoster
import com.example.movielookupapp.view.theme.MovieLookupAppTheme
import com.example.movielookupapp.viewmodel.MovieDetailState
import java.util.Locale

// Reached by passing movieId through navigation (R1). Stateless: AppNavHost gives it
// the state and lambdas, so it never sees a ViewModel or NavController (Lecture 6).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    state: MovieDetailState,
    isSaved: Boolean,
    viewings: List<Viewing>,
    onRetry: () -> Unit,
    onAddToWatchlist: (Movie) -> Unit,
    onShare: (Movie) -> Unit,
    onLogViewing: () -> Unit,
    onEditViewing: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                // A text button needs no icon library and is read out clearly by screen readers.
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("Back") }
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (state) {
            MovieDetailState.Loading -> LoadingView(modifier = contentModifier)

            is MovieDetailState.Error -> ErrorView(
                message = state.message,
                onRetry = onRetry,
                modifier = contentModifier
            )

            is MovieDetailState.Success -> DetailsContent(
                movie = state.movie,
                isOffline = state.isOffline,
                isSaved = isSaved,
                viewings = viewings,
                onAddToWatchlist = onAddToWatchlist,
                onShare = onShare,
                onLogViewing = onLogViewing,
                onEditViewing = onEditViewing,
                modifier = contentModifier
            )
        }
    }
}

@Composable
private fun DetailsContent(
    movie: Movie,
    isOffline: Boolean,
    isSaved: Boolean,
    viewings: List<Viewing>,
    onAddToWatchlist: (Movie) -> Unit,
    onShare: (Movie) -> Unit,
    onLogViewing: () -> Unit,
    onEditViewing: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // verticalScroll keeps everything reachable in landscape and with large text (R5).
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isOffline) {
            Text(
                text = "You're offline. Showing your saved copy.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        // widthIn caps the poster on a tablet so it doesn't fill the whole screen (Lecture 4 Part 8).
        MoviePoster(
            posterUrl = movie.posterUrl(),
            title = movie.title,
            modifier = Modifier
                .widthIn(max = 240.dp)
                .height(320.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = movie.releaseYear + "  •  ★ " + String.format(Locale.US, "%.1f", movie.rating),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // One filled button per screen for the main action (Lecture 4, Visibility).
        if (isSaved) {
            Button(
                onClick = onLogViewing,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Viewing")
            }
        } else {
            Button(
                onClick = { onAddToWatchlist(movie) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Watchlist")
            }
        }

        OutlinedButton(
            onClick = { onShare(movie) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share with a friend")
        }

        if (isSaved) {
            Text(
                text = "Your viewings",
                style = MaterialTheme.typography.titleMedium
            )

            if (viewings.isEmpty()) {
                Text(
                    text = "No viewings logged yet.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                // A short list inside a scrolling Column, so forEach rather than a
                // LazyColumn (Lecture 4: never nest a scroll inside a scroll).
                viewings.forEach { viewing ->
                    ViewingRow(
                        viewing = viewing,
                        onClick = { onEditViewing(viewing.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewingRow(
    viewing: Viewing,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = formatDate(viewing.watchedOn) + "  •  Rated ${viewing.rating}/5",
                style = MaterialTheme.typography.titleSmall
            )
            if (viewing.note.isNotBlank()) {
                Text(
                    text = viewing.note,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "Tap to edit",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenPreview() {
    MovieLookupAppTheme {
        MovieDetailsScreen(
            state = MovieDetailState.Success(
                Movie(
                    id = 1,
                    title = "Interstellar",
                    overview = "A team of explorers travels beyond this galaxy.",
                    posterPath = null,
                    releaseYear = "2014",
                    rating = 8.7
                ),
                isOffline = false
            ),
            isSaved = true,
            viewings = listOf(Viewing(1, 1, 0L, 5, "Watched with friends")),
            onRetry = {},
            onAddToWatchlist = {},
            onShare = {},
            onLogViewing = {},
            onEditViewing = {},
            onBackClick = {}
        )
    }
}
