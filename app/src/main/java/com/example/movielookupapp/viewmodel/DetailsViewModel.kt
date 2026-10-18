package com.example.movielookupapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielookupapp.model.local.MovieDatabase
import com.example.movielookupapp.model.remote.TmdbService
import com.example.movielookupapp.model.remote.isUsable
import com.example.movielookupapp.model.local.toMovie // WatchlistItem -> Movie (offline copy)
import com.example.movielookupapp.model.remote.toMovie // MovieDto -> Movie (from TMDB)
import kotlinx.coroutines.launch

// Details screen. AndroidViewModel (not plain ViewModel) gives us `application`,
// which MovieDatabase.get() needs, while still letting the screen use viewModel().
class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val watchlistDao = MovieDatabase.get(application).watchlistDao()

    var state by mutableStateOf<MovieDetailState>(MovieDetailState.Loading)
        private set

    private var movieId = 0

    // Called with the movieId that came through navigation (R1).
    fun load(id: Int) {
        // Already showing this movie (e.g. after rotation): don't fetch it again.
        if (id == movieId && state is MovieDetailState.Success) return
        movieId = id
        fetch()
    }

    fun retry() {
        fetch()
    }

    private fun fetch() {
        val id = movieId
        state = MovieDetailState.Loading
        viewModelScope.launch {
            val result = try {
                val dto = TmdbService.api.getMovie(id)
                if (dto.isUsable()) {
                    MovieDetailState.Success(dto.toMovie(), isOffline = false)
                } else {
                    MovieDetailState.Error("TMDB sent incomplete details for this movie.")
                }
            } catch (e: Exception) {
                // A7 offline: fall back to the copy saved in Room, if there is one.
                val saved = watchlistDao.getById(id)
                if (saved != null) {
                    MovieDetailState.Success(saved.toMovie(), isOffline = true)
                } else {
                    MovieDetailState.Error("Couldn't load this movie. Check your internet connection and try again.")
                }
            }
            if (id == movieId) {
                state = result
            }
        }
    }
}
