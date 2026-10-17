package com.example.moviewatchlist.model.local

import androidx.room.Embedded
import androidx.room.Relation

// A watchlist movie together with all of its viewings, loaded by Room in one call.
// Status, average rating and last watched date are NOT stored; WatchlistRepository (A4)
// calculates them from this list, so they can never get out of sync with the viewings.
data class WatchlistWithViewings(
    @Embedded val item: WatchlistItem,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "movieId"
    )
    val viewings: List<Viewing>
)
