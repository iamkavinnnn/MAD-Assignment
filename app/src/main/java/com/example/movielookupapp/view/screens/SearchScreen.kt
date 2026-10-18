package com.example.movielookupapp.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielookupapp.view.components.EmptyView
import com.example.movielookupapp.view.components.ErrorView
import com.example.movielookupapp.view.components.LoadingView
import com.example.movielookupapp.view.components.MovieGrid
import com.example.movielookupapp.view.theme.MovieLookupAppTheme
import com.example.movielookupapp.viewmodel.MovieListState

// Stateless: the typed text lives in SearchViewModel, so it survives rotation.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    query: String,
    state: MovieListState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Movie title") },
                    placeholder = { Text("Search for a movie") },
                    singleLine = true,
                    // The keyboard's own search key also runs the search.
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() })
                )
                Button(onClick = onSearch) {
                    Text("Search")
                }
            }

            when (state) {
                MovieListState.Idle -> EmptyView(
                    message = "Type a movie title, then tap Search.",
                    modifier = Modifier.fillMaxWidth()
                )

                MovieListState.Loading -> LoadingView(
                    modifier = Modifier.fillMaxWidth(),
                    message = "Searching movies..."
                )

                is MovieListState.Error -> ErrorView(
                    message = state.message,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxWidth()
                )

                // R4 empty state: the request worked, but nothing usable came back.
                MovieListState.Empty -> EmptyView(
                    message = "No movies found for \"$query\".",
                    modifier = Modifier.fillMaxWidth()
                )

                is MovieListState.Success -> MovieGrid(
                    movies = state.movies,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    MovieLookupAppTheme {
        SearchScreen(
            query = "zzqxv",
            state = MovieListState.Empty,
            onQueryChange = {},
            onSearch = {},
            onRetry = {},
            onMovieClick = {}
        )
    }
}
