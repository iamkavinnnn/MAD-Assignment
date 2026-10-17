package com.example.moviewatchlist.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WatchlistItem::class, Viewing::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun watchlistDao(): WatchlistDao
    abstract fun viewingDao(): ViewingDao

    companion object {
        // @Volatile so every thread sees the same instance as soon as it is created.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Only one database is ever opened. synchronized stops two threads creating it at the same time.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_watchlist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
