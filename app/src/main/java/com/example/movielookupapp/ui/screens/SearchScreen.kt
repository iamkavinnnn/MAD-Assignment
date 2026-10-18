package com.example.movielookupapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movielookupapp.ui.components.EmptyView
import com.example.movielookupapp.ui.components.ErrorView
import com.example.movielookupapp.ui.components.LoadingView
import com.example.movielookupapp.ui.components.MovieCard
import com.example.moviewatchlist.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit = {},
    searchViewModel: SearchViewModel = viewModel()
) {
    val uiState by searchViewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Search Movies") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = searchViewModel::onQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Movie title") },
                placeholder = { Text("Search for a movie") },
                singleLine = true
            )

            when {
                uiState.query.isBlank() -> {
                    EmptyView(
                        message = "Enter a movie title to search.",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                uiState.isLoading -> {
                    LoadingView(
                        modifier = Modifier.fillMaxWidth(),
                        message = "Searching movies..."
                    )
                }

                uiState.error != null -> {
                    ErrorView(
                        message = uiState.error ?: "Search failed.",
                        onRetry = {
                            searchViewModel.onQueryChanged(uiState.query)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                uiState.movies.isEmpty() -> {
                    EmptyView(
                        message = "No movies found.",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.movies,
                            key = { movie -> movie.id }
                        ) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}