package com.example.movielookupapp.model

import com.example.movielookupapp.model.local.SortOrder
import com.example.movielookupapp.model.local.Viewing
import com.example.movielookupapp.model.local.WatchlistItem

// This is where the relationship between the two tables is used by the core task.
data class WatchlistEntry(
    val item: WatchlistItem,
    val status: String,          // "Planning to watch" or "Watched"
    val viewingCount: Int,
    val averageRating: Double?,  // null if never watched
    val lastWatched: Long?       // null if never watched
)

// Joins each saved movie to its own viewings, using movieId
// ref:(Lecture 7, foreign keys).
fun buildEntries(items: List<WatchlistItem>, viewings: List<Viewing>): List<WatchlistEntry> =
    items.map { item ->
        val mine = viewings.filter { it.movieId == item.movieId }
        WatchlistEntry(
            item = item,
            status = if (mine.isEmpty()) "Plan to watch" else "Watched",
            viewingCount = mine.size,
            averageRating = if (mine.isEmpty()) null else mine.map { it.rating }.average(),
            lastWatched = mine.maxOfOrNull { it.watchedOn }
        )
    }

// Applies the sort order saved in DataStore (UserPreferences.kt).
fun sortEntries(entries: List<WatchlistEntry>, sortOrder: String): List<WatchlistEntry> =
    when (sortOrder) {
        SortOrder.TITLE -> entries.sortedBy { it.item.title.lowercase() }
        SortOrder.RATING -> entries.sortedByDescending { it.averageRating ?: -1.0 }
        else -> entries.sortedByDescending { it.item.addedAt }
    }
