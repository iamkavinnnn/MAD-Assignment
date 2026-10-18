package com.example.movielookupapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ViewingDao {

    @Query("SELECT * FROM viewings")
    suspend fun getAll(): List<Viewing>

    // The relationship in use: every viewing that belongs to one movie.
    @Query("SELECT * FROM viewings WHERE movieId = :movieId ORDER BY watchedOn DESC")
    suspend fun getForMovie(movieId: Int): List<Viewing>

    // Loads one viewing into the form when the user edits it.
    @Query("SELECT * FROM viewings WHERE id = :id")
    suspend fun getById(id: Int): Viewing?

    @Insert
    suspend fun insert(viewing: Viewing)

    @Update
    suspend fun update(viewing: Viewing)

    @Delete
    suspend fun delete(viewing: Viewing)

    // Called before removing a movie, so no viewing points at a movie that no longer exists.
    @Query("DELETE FROM viewings WHERE movieId = :movieId")
    suspend fun deleteForMovie(movieId: Int)
}
