package com.example.movielookupapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movielookupapp.model.Movie

// Table 1: one row per saved movie (the "one" side, like "courses" in Lecture 7).
// movieId is TMDB's id, not autoGenerate, so the same movie cannot be saved twice.
// The details are copied in so the app still shows saved movies offline.
@Entity(tableName = "watchlist_items")
data class WatchlistItem(
    @PrimaryKey val movieId: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseYear: String,
    val rating: Double,
    val addedAt: Long // System.currentTimeMillis() when saved
)

fun Movie.toWatchlistItem(): WatchlistItem = WatchlistItem(
    movieId = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseYear = releaseYear,
    rating = rating,
    addedAt = System.currentTimeMillis()
)

fun WatchlistItem.toMovie(): Movie = Movie(
    id = movieId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseYear = releaseYear,
    rating = rating
)
