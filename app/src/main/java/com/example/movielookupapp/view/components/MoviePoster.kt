package com.example.movielookupapp.view.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

// Shows the poster, or the placeholder when there is no poster or it fails to load
// (part of the R4 bad-data handling: a missing poster_path never breaks the screen).
// Taken out of MovieCard so the Details screen can use it too.
@Composable
fun MoviePoster(
    posterUrl: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    var imageError by remember(posterUrl) { mutableStateOf(false) }

    if (posterUrl == null || imageError) {
        MoviePosterPlaceholder(modifier = modifier)
    } else {
        AsyncImage(
            model = posterUrl,
            contentDescription = "Poster for $title",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            onError = { imageError = true }
        )
    }
}
