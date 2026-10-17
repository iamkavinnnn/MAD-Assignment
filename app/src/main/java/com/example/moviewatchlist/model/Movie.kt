package com.example.moviewatchlist.model // change to match your project's package

// The one movie type the ViewModels and screens use.
// It is built from either a TMDB response or a saved WatchlistItem, so the UI never sees DTOs or entities.
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?, // null means "show the placeholder image"
    val releaseYear: String,
    val voteAverage: Double,
    val runtimeMinutes: Int? = null, // only the details endpoint returns this
    val genres: List<String> = emptyList(),
)
