package com.example.movielookupapp.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielookupapp.model.Movie

// The poster grid shared by Trending and Search.
// GridCells.Adaptive fits more columns on wider screens, so phone portrait, landscape
// and tablet all work without a special tablet layout (R5, Lecture 4 Part 8).
// key = movie.id keeps each card's state with the right movie (Lecture 5, scenario 3).
@Composable
fun MovieGrid(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = movies,
            key = { movie -> movie.id }
        ) { movie ->
            MovieCard(
                movie = movie,
                onClick = { onMovieClick(movie.id) }
            )
        }
    }
}
