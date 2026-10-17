package com.example.moviewatchlist.model.remote

import com.example.moviewatchlist.model.Movie

// R4 bad-data handling lives here, so every screen gets the same clean Movie.
//   - no id, or no title at all      -> the record is dropped
//   - blank overview                 -> "No overview available."
//   - null or blank poster_path      -> posterUrl = null (UI shows a placeholder)
//   - empty or malformed release_date -> "Unknown"
//   - missing or out-of-range rating -> clamped to 0.0..10.0
//   - same movie on two pages        -> removed by id

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val NO_OVERVIEW = "No overview available."
private const val UNKNOWN_YEAR = "Unknown"

fun MovieDto.toMovie(): Movie? {
    val movieId = id ?: return null
    val movieTitle = cleanTitle(title, originalTitle) ?: return null

    return Movie(
        id = movieId,
        title = movieTitle,
        overview = cleanOverview(overview),
        posterUrl = posterUrlFrom(posterPath),
        releaseYear = releaseYearFrom(releaseDate),
        voteAverage = cleanRating(voteAverage),
    )
}

fun MovieDetailsDto.toMovie(): Movie? {
    val movieId = id ?: return null
    val movieTitle = cleanTitle(title, originalTitle) ?: return null

    return Movie(
        id = movieId,
        title = movieTitle,
        overview = cleanOverview(overview),
        posterUrl = posterUrlFrom(posterPath),
        releaseYear = releaseYearFrom(releaseDate),
        voteAverage = cleanRating(voteAverage),
        runtimeMinutes = runtime?.takeIf { it > 0 },
        genres = genres.orEmpty().mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } },
    )
}

// Used by Trending and Search: drop broken records, then remove duplicates by id.
fun MovieListResponseDto.toMovies(): List<Movie> =
    results.orEmpty()
        .mapNotNull { it.toMovie() }
        .distinctBy { it.id }

private fun cleanTitle(title: String?, originalTitle: String?): String? =
    title?.trim()?.takeIf { it.isNotEmpty() }
        ?: originalTitle?.trim()?.takeIf { it.isNotEmpty() }

private fun cleanOverview(overview: String?): String =
    overview?.trim()?.takeIf { it.isNotEmpty() } ?: NO_OVERVIEW

private fun posterUrlFrom(posterPath: String?): String? =
    posterPath?.trim()?.takeIf { it.isNotEmpty() }?.let { IMAGE_BASE_URL + it }

// TMDB dates look like "2024-03-01", but can be "" or missing. Only accept a real 4-digit year.
private fun releaseYearFrom(releaseDate: String?): String {
    val year = releaseDate?.trim()?.take(4)
    return if (year != null && year.length == 4 && year.all { it.isDigit() }) year else UNKNOWN_YEAR
}

private fun cleanRating(voteAverage: Double?): Double {
    if (voteAverage == null || voteAverage.isNaN()) return 0.0
    return voteAverage.coerceIn(0.0, 10.0)
}
