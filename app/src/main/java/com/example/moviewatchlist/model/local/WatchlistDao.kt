package com.example.moviewatchlist.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    // IGNORE, and not REPLACE: REPLACE deletes the old row first, which would CASCADE-delete its viewings.
    // Returns -1 if the movie was already saved.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchlistItem(item: WatchlistItem): Long

    @Update
    suspend fun updateWatchlistItem(item: WatchlistItem)

    @Delete
    suspend fun deleteWatchlistItem(item: WatchlistItem)

    // @Transaction because Room runs two queries for @Relation (items, then viewings).
    // Sorting by the user's preference happens in WatchlistViewModel (A6), not here.
    @Transaction
    @Query("SELECT * FROM watchlist_items ORDER BY addedAt DESC")
    fun getWatchlistWithViewings(): Flow<List<WatchlistWithViewings>>

    @Transaction
    @Query("SELECT * FROM watchlist_items WHERE movieId = :movieId")
    fun getWatchlistItemWithViewings(movieId: Int): Flow<WatchlistWithViewings?>

    // Used by the Details screen to show "Add to watchlist" or "In watchlist".
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_items WHERE movieId = :movieId)")
    fun isInWatchlist(movieId: Int): Flow<Boolean>
}
