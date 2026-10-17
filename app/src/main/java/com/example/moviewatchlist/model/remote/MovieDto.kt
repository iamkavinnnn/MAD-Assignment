package com.example.moviewatchlist.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Every field is nullable with a default because TMDB often leaves fields out or sends null.
// A missing field becomes null instead of crashing the JSON parser; MovieMapper decides what to do with it.

@Serializable
data class MovieListResponseDto(
    val page: Int? = null,
    val results: List<MovieDto>? = null,
    @SerialName("total_pages") val totalPages: Int? = null,
)

@Serializable
data class MovieDto(
    val id: Int? = null,
    val title: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
)

// Named MovieDetailsDto, and not MovieDto, because movie/{id} returns extra fields (runtime, genres).
@Serializable
data class MovieDetailsDto(
    val id: Int? = null,
    val title: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    val runtime: Int? = null,
    val genres: List<GenreDto>? = null,
)

@Serializable
data class GenreDto(
    val id: Int? = null,
    val name: String? = null,
)
