package com.example.moviewatchlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviewatchlist.model.Movie
import com.example.moviewatchlist.model.remote.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

class SearchViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        _uiState.value = SearchUiState(query = query)
        searchJob?.cancel()

        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            delay(400)

            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val movies = repository.searchMovies(query)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = movies
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Unable to search movies. Please try again."
                )
            }
        }
    }
}