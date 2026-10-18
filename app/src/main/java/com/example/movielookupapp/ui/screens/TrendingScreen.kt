package com.example.movielookupapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movielookupapp.ui.components.MovieCard
import com.example.moviewatchlist.viewmodel.TrendingViewModel
import com.example.movielookupapp.ui.components.LoadingView
import com.example.movielookupapp.ui.components.EmptyView
import com.example.movielookupapp.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit = {},
    trendingViewModel: TrendingViewModel = viewModel()
) {
    val uiState by trendingViewModel.uiState.collectAsState()

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

        when {
            uiState.isLoading -> LoadingView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            uiState.error != null -> ErrorView(
                message = uiState.error ?: "Something went wrong.",
                onRetry = { trendingViewModel.loadTrendingMovies() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            uiState.movies.isEmpty() -> EmptyView(
                message = "No trending movies found.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.movies,
                        key = { movie -> movie.id }
                    ) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = {
                                onMovieClick(movie.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
