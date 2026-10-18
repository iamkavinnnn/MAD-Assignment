package com.example.movielookupapp.viewmodel

import com.example.movielookupapp.model.Movie

// Lecture 2's ScreenState pattern: the screen is in exactly one of these states at a time,
// and the compiler makes the screen's `when` handle every one (R4).

sealed class MovieListState {
    object Idle : MovieListState()    // nothing searched yet (Search screen only)
    object Loading : MovieListState()
    data class Success(val movies: List<Movie>) : MovieListState()
    object Empty : MovieListState()   // the request worked but found no usable movies
    data class Error(val message: String) : MovieListState()
}

sealed class MovieDetailState {
    object Loading : MovieDetailState()
    // isOffline = true means the details came from Room because the network failed.
    data class Success(val movie: Movie, val isOffline: Boolean) : MovieDetailState()
    data class Error(val message: String) : MovieDetailState()
}
