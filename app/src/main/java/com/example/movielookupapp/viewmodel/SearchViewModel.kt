package com.example.movielookupapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielookupapp.model.remote.TmdbService
import com.example.movielookupapp.model.remote.toMovies
import kotlinx.coroutines.launch

// Search screen (the second tab). Searches TMDB when the user taps Search.
class SearchViewModel : ViewModel() {

    // Kept here, not in the screen, so the typed text survives rotation (Lecture 5).
    var query by mutableStateOf("")
        private set

    var state by mutableStateOf<MovieListState>(MovieListState.Idle)
        private set

    // The last title actually searched for. Retry repeats it.
    private var lastSearch = ""

    fun onQueryChange(text: String) {
        query = text
        if (text.isBlank()) {
            lastSearch = ""
            state = MovieListState.Idle
        }
    }

    fun search() {
        val text = query.trim()
        if (text.isEmpty()) {
            state = MovieListState.Idle
            return
        }
        load(text)
    }

    fun retry() {
        if (lastSearch.isNotEmpty()) load(lastSearch)
    }

    private fun load(searchText: String) {
        lastSearch = searchText
        state = MovieListState.Loading
        viewModelScope.launch {
            val result = try {
                val movies = TmdbService.api.searchMovies(searchText).results.toMovies()
                if (movies.isEmpty()) MovieListState.Empty else MovieListState.Success(movies)
            } catch (e: Exception) {
                MovieListState.Error("Couldn't search right now. Check your internet connection and try again.")
            }
            // Ignore an old answer if the user has searched again (or cleared the box) since.
            if (searchText == lastSearch) {
                state = result
            }
        }
    }
}
