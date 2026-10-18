package com.example.movielookupapp.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// The JSON shapes TMDB sends back. "Dto" = data transfer object.
// Every field is nullable with a default (Lecture 1, nullable types), because TMDB
// sometimes leaves fields out or sends null. MovieMapper decides what to do with them.

@Serializable
data class MovieListResponse(
    val results: List<MovieDto> = emptyList()
)

@Serializable
data class MovieDto(
    val id: Int? = null,
    val title: String? = null,
    val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null
)
