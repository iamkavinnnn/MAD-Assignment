package com.example.movielookupapp.view

import kotlinx.serialization.Serializable

// Type-safe routes (Lecture 6). Each screen is a Kotlin type, so a typo is a compile error.

// The three bottom-bar tabs
@Serializable
object TrendingRoute

@Serializable
object SearchRoute

@Serializable
object WatchlistRoute

// The detail screen is reached by passing an ID through navigation (R1).
@Serializable
data class DetailsRoute(val movieId: Int)

// viewingId = 0 means "log a new viewing"; any other value means "edit that viewing".
@Serializable
data class LogViewingRoute(val movieId: Int, val viewingId: Int = 0)
