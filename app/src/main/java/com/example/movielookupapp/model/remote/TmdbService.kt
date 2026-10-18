package com.example.movielookupapp.model.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// One shared Retrofit instance for the whole app, the same idea as keeping one
// database instance in MovieDatabase (Lecture 7: opening it is expensive, so keep the first one).
object TmdbService {

    // ignoreUnknownKeys: TMDB sends many fields we never use; without this, parsing fails.
    private val json = Json { ignoreUnknownKeys = true }

    val api: TmdbApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(TmdbApi::class.java)
}
