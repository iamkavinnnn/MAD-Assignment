package com.example.movielookupapp.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Same shape as NotesDb in Lecture 7: entities and version, one function per DAO,
// and a single instance kept in the companion object.
// exportSchema = false: we don't keep schema history files, which only matter for migrations.
@Database(entities = [WatchlistItem::class, Viewing::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun watchlistDao(): WatchlistDao
    abstract fun viewingDao(): ViewingDao

    companion object {
        private var db: MovieDatabase? = null

        fun get(context: Context): MovieDatabase {
            if (db == null) {
                db = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "movies.db"
                ).build()
            }
            return db!!
        }
    }
}
