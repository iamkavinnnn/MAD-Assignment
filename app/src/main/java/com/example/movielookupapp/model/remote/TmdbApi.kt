package com.example.movielookupapp.model.remote

import com.example.movielookupapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// The three TMDB requests the app makes as main functionalities.
// Each one is suspend, like the Room DAO
// functions in Lecture 7, so it is called from viewModelScope.launch { }.
// apiKey has a default value stored in the local.properties file.
interface TmdbApi {

    @GET("trending/movie/week")
    suspend fun getTrending(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): MovieListResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): MovieListResponse

    @GET("movie/{id}")
    suspend fun getMovie(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): MovieDto
}
