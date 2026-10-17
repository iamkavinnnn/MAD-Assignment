package com.example.moviewatchlist.model.local

import com.example.moviewatchlist.model.Movie

// Movie -> WatchlistItem when the user taps "Add to watchlist".
fun Movie.toWatchlistItem(addedAt: Long = System.currentTimeMillis()): WatchlistItem =
    WatchlistItem(
        movieId = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        voteAverage = voteAverage,
        addedAt = addedAt,
    )

// WatchlistItem -> Movie when the Details screen is offline and falls back to Room (A7).
fun WatchlistItem.toMovie(): Movie =
    Movie(
        id = movieId,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        voteAverage = voteAverage,
    )
