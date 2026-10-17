package com.example.moviewatchlist.model.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// The three TMDB endpoints the app uses. The api_key is added to every request in RetrofitInstance.
interface TmdbApi {

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1
    ): MovieListResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieListResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetailsDto
}
