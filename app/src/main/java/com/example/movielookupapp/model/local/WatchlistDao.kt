package com.example.movielookupapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist_items ORDER BY addedAt DESC")
    suspend fun getAll(): List<WatchlistItem>

    // Used by the Details screen when there is no internet.
    @Query("SELECT * FROM watchlist_items WHERE movieId = :movieId")
    suspend fun getById(movieId: Int): WatchlistItem?

    // IGNORE: saving a movie that is already saved does nothing, instead of crashing.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WatchlistItem)

    @Delete
    suspend fun delete(item: WatchlistItem)
}
