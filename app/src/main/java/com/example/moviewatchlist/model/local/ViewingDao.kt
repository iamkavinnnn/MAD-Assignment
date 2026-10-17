package com.example.moviewatchlist.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ViewingDao {

    @Insert
    suspend fun insertViewing(viewing: Viewing): Long

    @Update
    suspend fun updateViewing(viewing: Viewing)

    @Delete
    suspend fun deleteViewing(viewing: Viewing)

    // suspend, and not Flow: the Log Viewing form only needs to load an existing viewing once, for editing.
    @Query("SELECT * FROM viewings WHERE id = :viewingId")
    suspend fun getViewingById(viewingId: Int): Viewing?

    @Query("SELECT * FROM viewings WHERE movieId = :movieId ORDER BY watchedOn DESC")
    fun getViewingsForMovie(movieId: Int): Flow<List<Viewing>>
}
