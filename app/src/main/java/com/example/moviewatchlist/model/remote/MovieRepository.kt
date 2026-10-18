package com.example.moviewatchlist.model.remote

import com.example.moviewatchlist.model.Movie

class MovieRepository(
    private val api: TmdbApi = RetrofitInstance.api
) {
    suspend fun getTrendingMovies(): List<Movie> {
        return api.getTrendingMovies().toMovies()
    }

    suspend fun searchMovies(query: String): List<Movie> {
        if (query.isBlank()) return emptyList()
        return api.searchMovies(query.trim()).toMovies()
    }

    suspend fun getMovieDetails(movieId: Int): Movie {
        val movie = api.getMovieDetails(movieId).toMovie()

        return requireNotNull(movie) {
            "Movie details were missing a valid movie ID or title."
        }
    }
}
