package com.example.moviewatchlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviewatchlist.model.Movie
import com.example.moviewatchlist.model.remote.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TrendingUiState(
    val isLoading: Boolean = true,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

class TrendingViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrendingUiState())
    val uiState: StateFlow<TrendingUiState> = _uiState.asStateFlow()

    init {
        loadTrendingMovies()
    }

    fun loadTrendingMovies() {
        viewModelScope.launch {
            _uiState.value = TrendingUiState(isLoading = true)

            try {
                val movies = repository.getTrendingMovies()

                _uiState.value = TrendingUiState(
                    isLoading = false,
                    movies = movies
                )
            } catch (exception: Exception) {
                _uiState.value = TrendingUiState(
                    isLoading = false,
                    error = "Unable to load trending movies. Check your connection and try again."
                )
            }
        }
    }
}
