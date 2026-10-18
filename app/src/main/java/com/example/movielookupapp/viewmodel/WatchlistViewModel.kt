package com.example.movielookupapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielookupapp.model.Movie
import com.example.movielookupapp.model.local.MovieDatabase
import com.example.movielookupapp.model.local.SortOrder
import com.example.movielookupapp.model.local.UserPreferences
import com.example.movielookupapp.model.local.Viewing
import com.example.movielookupapp.model.WatchlistEntry
import com.example.movielookupapp.model.local.WatchlistItem
import com.example.movielookupapp.model.buildEntries
import com.example.movielookupapp.model.parseDate
import com.example.movielookupapp.model.sortEntries
import com.example.movielookupapp.model.local.toWatchlistItem
import com.example.movielookupapp.model.local.validationError
import kotlinx.coroutines.launch

// Watchlist and Log Viewing screens. Same pattern as NotesViewModel in Lecture 7:
// every write is a viewModelScope.launch, followed by reload() so the screen updates.
// Everything here works with no internet (A7), because it only uses Room and DataStore.
class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MovieDatabase.get(application)
    private val watchlistDao = db.watchlistDao()
    private val viewingDao = db.viewingDao()
    private val preferences = UserPreferences(application)

    var entries by mutableStateOf(listOf<WatchlistEntry>())
        private set

    var sortOrder by mutableStateOf(SortOrder.DATE_ADDED)
        private set

    private var allViewings = listOf<Viewing>()

    init {
        viewModelScope.launch {
            sortOrder = preferences.getSortOrder()
            reload()
        }
    }

    private suspend fun reload() {
        val items = watchlistDao.getAll()
        allViewings = viewingDao.getAll()
        entries = sortEntries(buildEntries(items, allViewings), sortOrder)
    }

    // ---- Watchlist ----

    fun isSaved(movieId: Int): Boolean = entries.any { it.item.movieId == movieId }

    fun add(movie: Movie) = viewModelScope.launch {
        watchlistDao.insert(movie.toWatchlistItem())
        reload()
    }

    // Two writes: the movie's viewings first, so none is left pointing at a deleted movie.
    fun remove(item: WatchlistItem) = viewModelScope.launch {
        viewingDao.deleteForMovie(item.movieId)
        watchlistDao.delete(item)
        reload()
    }

    // R3: saved in DataStore, so the order is the same after the app is closed and reopened.
    fun changeSortOrder(order: String) = viewModelScope.launch {
        preferences.setSortOrder(order)
        sortOrder = order
        entries = sortEntries(entries, order)
    }

    // ---- Viewings ----

    fun viewingsFor(movieId: Int): List<Viewing> =
        allViewings.filter { it.movieId == movieId }.sortedByDescending { it.watchedOn }

    // viewingId 0 means "new viewing", so there is nothing to load.
    fun findViewing(viewingId: Int): Viewing? =
        allViewings.find { it.id == viewingId }

    // Like addContact() in Lecture 5: the screen calls this, and gets back null if it was
    // saved, or a message to show if it was rejected. viewingId 0 = add, otherwise update.
    fun saveViewing(movieId: Int, viewingId: Int, dateText: String, rating: Int, note: String): String? {
        if (!isSaved(movieId)) return "Add this movie to your watchlist first."
        val watchedOn = parseDate(dateText)
            ?: return "Enter the date as YYYY-MM-DD, for example 2026-10-18."

        val viewing = Viewing(
            id = viewingId,
            movieId = movieId,
            watchedOn = watchedOn,
            rating = rating,
            note = note.trim()
        )
        val error = viewing.validationError() // the rule lives in the Model
        if (error != null) return error

        viewModelScope.launch {
            if (viewingId == 0) viewingDao.insert(viewing) else viewingDao.update(viewing)
            reload()
        }
        return null
    }

    fun deleteViewing(viewing: Viewing) = viewModelScope.launch {
        viewingDao.delete(viewing)
        reload()
    }
}
