package com.example.movielookupapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielookupapp.ui.components.MovieCard
import com.example.moviewatchlist.model.Movie

private val sampleMovies = listOf(
    Movie(
        id = 1,
        title = "Interstellar",
        overview = "A team travels beyond this galaxy to explore the limits of space and time.",
        posterUrl = null,
        releaseYear = "2014",
        voteAverage = 8.7
    ),
    Movie(
        id = 2,
        title = "Inception",
        overview = "A skilled thief enters dreams to steal valuable secrets.",
        posterUrl = null,
        releaseYear = "2010",
        voteAverage = 8.4
    ),
    Movie(
        id = 3,
        title = "The Dark Knight",
        overview = "Batman faces a dangerous criminal threatening Gotham.",
        posterUrl = null,
        releaseYear = "2008",
        voteAverage = 9.0
    ),
    Movie(
        id = 4,
        title = "The Matrix",
        overview = "A computer hacker discovers the truth behind his reality.",
        posterUrl = null,
        releaseYear = "1999",
        voteAverage = 8.2
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit = {}
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
                items = sampleMovies,
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
