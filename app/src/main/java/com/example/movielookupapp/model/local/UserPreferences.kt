package com.example.movielookupapp.model.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// The three ways the watchlist can be sorted. Stored in DataStore as plain text.
object SortOrder {
    val DATE_ADDED = "date_added"
    val TITLE = "title"
    val RATING = "rating"
}

// One DataStore file for the whole app, named "settings".
// Lecture 7's table: DataStore is for "small key-value settings", kept until uninstall.
private val Context.dataStore by preferencesDataStore(name = "settings")

class UserPreferences(private val context: Context) {

    private val sortKey = stringPreferencesKey("sort_order")

    // suspend, like the DAO functions: read once, the same way NotesViewModel calls getAll().
    suspend fun getSortOrder(): String =
        context.dataStore.data.first()[sortKey] ?: SortOrder.DATE_ADDED

    suspend fun setSortOrder(order: String) {
        context.dataStore.edit { prefs -> prefs[sortKey] = order }
    }
}
