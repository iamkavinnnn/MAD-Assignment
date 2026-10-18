package com.example.movielookupapp.model // change to match your project's package

import java.util.Locale

// The movie the ViewModels and screens use.
// Built from a TMDB response (MovieMapper.kt) or from a saved WatchlistItem.
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?, // null means TMDB sent no poster
    val releaseYear: String,
    val rating: Double
)

// Full web address of the poster image, or null if there is none (the screen then
// shows MoviePosterPlaceholder). TMDB only sends the end of the address, e.g. "/abc.jpg".
fun Movie.posterUrl(): String? =
    if (posterPath == null) null else "https://image.tmdb.org/t/p/w500$posterPath"

// The text the Share button sends (R6). Building it is data work, so it lives here,
// not in the composable. Locale.US keeps "7.5" from becoming "7,5" (Lecture 3, PriceTag).
fun Movie.shareText(): String =
    "Watch this with me: $title ($releaseYear), rated " +
        String.format(Locale.US, "%.1f", rating) + "/10 on TMDB"
