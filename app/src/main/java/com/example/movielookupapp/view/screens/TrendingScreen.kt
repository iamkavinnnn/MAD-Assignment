package com.example.movielookupapp.view.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movielookupapp.model.Movie
import com.example.movielookupapp.view.components.EmptyView
import com.example.movielookupapp.view.components.ErrorView
import com.example.movielookupapp.view.components.LoadingView
import com.example.movielookupapp.view.components.MovieGrid
import com.example.movielookupapp.view.theme.MovieLookupAppTheme
import com.example.movielookupapp.viewmodel.MovieListState

// Stateless: gets the state and lambdas from AppNavHost (Lecture 3 state hoisting).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    state: MovieListState,
    onRetry: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trending Movies",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        // R4: one branch per state. The sealed class makes the compiler check none is missed.
        when (state) {
            MovieListState.Idle,
            MovieListState.Loading -> LoadingView(modifier = contentModifier)

            is MovieListState.Error -> ErrorView(
                message = state.message,
                onRetry = onRetry,
                modifier = contentModifier
            )

            MovieListState.Empty -> EmptyView(
                message = "No trending movies found.",
                modifier = contentModifier
            )

            is MovieListState.Success -> MovieGrid(
                movies = state.movies,
                onMovieClick = onMovieClick,
                modifier = contentModifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrendingScreenPreview() {
    MovieLookupAppTheme {
        TrendingScreen(
            state = MovieListState.Success(
                listOf(
                    Movie(1, "Interstellar", "A journey through space.", null, "2014", 8.7),
                    Movie(2, "Inception", "A thief enters dreams.", null, "2010", 8.4)
                )
            ),
            onRetry = {},
            onMovieClick = {}
        )
    }
}
