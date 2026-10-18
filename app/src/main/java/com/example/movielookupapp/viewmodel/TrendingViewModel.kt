package com.example.movielookupapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielookupapp.model.remote.TmdbService
import com.example.movielookupapp.model.remote.toMovies
import kotlinx.coroutines.launch
import android.util.Log

// Trending screen(homepage). loads this weeks trending movies once, and again on retry.
class TrendingViewModel : ViewModel() {

    var state by mutableStateOf<MovieListState>(MovieListState.Loading)
        private set

    init {
        load()
    }

    fun retry() {
        load()
    }

    private fun load() {
        state = MovieListState.Loading
        viewModelScope.launch {
            state = try {
                // tries to get movie data from TMDB server.
                val movies = TmdbService.api.getTrending().results.toMovies()
                if (movies.isEmpty()) MovieListState.Empty else MovieListState.Success(movies)
            } catch (e: Exception) {
                // catches and logs an error when there is no internet or any other server error.
                // ref: https://developer.android.com/reference/kotlin/android/util/Log
                Log.e("TMDB", "Trending request failed", e)
                MovieListState.Error("Couldn't load trending movies. Check your internet connection and try again.")
            }
        }
    }
}
