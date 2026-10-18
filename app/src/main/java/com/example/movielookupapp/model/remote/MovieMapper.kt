package com.example.movielookupapp.model.remote

import com.example.movielookupapp.model.Movie

// R4 bad-data handling. These rules belong to the data, so they live in the Model
// (Lecture 5: "the Model defines the rule").
//   - no id or no title          -> the movie is dropped
//   - blank overview             -> "No overview available."
//   - blank poster_path          -> null (the screen shows a placeholder)
//   - empty or malformed date    -> "Unknown"
//   - missing or out-of-range rating -> kept between 0.0 and 10.0
//   - same movie listed twice    -> duplicates removed by id

fun MovieDto.isUsable(): Boolean = id != null && !title.isNullOrBlank()

fun MovieDto.toMovie(): Movie {
    val year = releaseDate?.take(4) ?: ""
    return Movie(
        id = id ?: 0,
        title = title?.trim() ?: "",
        overview = if (overview.isNullOrBlank()) "No overview available." else overview.trim(),
        posterPath = if (posterPath.isNullOrBlank()) null else posterPath,
        releaseYear = if (year.length == 4 && year.all { it.isDigit() }) year else "Unknown",
        rating = (voteAverage ?: 0.0).coerceIn(0.0, 10.0)
    )
}

// Filter, then transform, then tidy up (Lecture 2, chaining operations).
fun List<MovieDto>.toMovies(): List<Movie> =
    this.filter { it.isUsable() }
        .map { it.toMovie() }
        .distinctBy { it.id }
