package com.example.moviewatchlist.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// One row per saved movie (the "one" side of the relationship).
// movieId is TMDB's id, and not an auto-generated id, so the same movie cannot be saved twice.
// The movie details are copied in here so the Details screen still works offline (A7).
@Entity(tableName = "watchlist_items")
data class WatchlistItem(
    @PrimaryKey val movieId: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseYear: String,
    val voteAverage: Double,
    val addedAt: Long, // System.currentTimeMillis() when saved; used for "sort by date added"
)
