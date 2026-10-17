package com.example.moviewatchlist.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// One row per time a movie was watched (the "many" side of the relationship).
// CASCADE: removing a movie from the watchlist also removes its viewings.
// The index on movieId keeps the join fast and stops Room's foreign key warning.
@Entity(
    tableName = "viewings",
    foreignKeys = [
        ForeignKey(
            entity = WatchlistItem::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("movieId")]
)
data class Viewing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val watchedOn: Long, // date watched, in milliseconds
    val rating: Int,     // 1 to 5; checked in LogViewingViewModel before saving
    val note: String,
)
