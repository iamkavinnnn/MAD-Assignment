package com.example.movielookupapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Table 2: one row per time a movie was watched (the "many" side, like "notes" in Lecture 7).
// movieId is the foreign key: it holds the primary key of a row in watchlist_items.
@Entity(tableName = "viewings")
data class Viewing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val watchedOn: Long, // date watched, in milliseconds
    val rating: Int,     // 1 to 5
    val note: String
)

// The rules that belong to the data (Lecture 5, Contact.isValid()).
// Returns a message for the user, or null if the viewing is fine.
// The ViewModel enforces it before saving.
fun Viewing.validationError(now: Long = System.currentTimeMillis()): String? =
    when {
        rating !in 1..5 -> "Choose a rating from 1 to 5."
        watchedOn > now -> "The date watched can't be in the future."
        note.length > 200 -> "Keep the note under 200 characters."
        else -> null
    }

fun Viewing.isValid(now: Long = System.currentTimeMillis()): Boolean =
    validationError(now) == null
