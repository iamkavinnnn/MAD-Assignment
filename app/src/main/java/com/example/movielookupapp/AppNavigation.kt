
package com.example.movielookupapp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movielookupapp.ui.screens.LogViewingScreen
import com.example.movielookupapp.ui.screens.MovieDetailsScreen
import com.example.movielookupapp.ui.screens.SearchScreen
import com.example.movielookupapp.ui.screens.TrendingScreen
import com.example.movielookupapp.ui.screens.WatchlistScreen

private object AppRoutes {
    const val TRENDING = "trending"
    const val SEARCH = "search"
    const val WATCHLIST = "watchlist"
    const val DETAILS = "details/{movieId}"
    const val LOG_VIEWING = "logViewing/{movieId}"

    fun details(movieId: Int) = "details/$movieId"
    fun logViewing(movieId: Int) = "logViewing/$movieId"
}

@Composable
fun MovieWatchlistNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomRoutes = listOf(
        AppRoutes.TRENDING,
        AppRoutes.SEARCH,
        AppRoutes.WATCHLIST
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomRoutes) {
                NavigationBar {
                    bottomRoutes.forEach { route ->
                        val label = when (route) {
                            AppRoutes.TRENDING -> "Trending"
                            AppRoutes.SEARCH -> "Search"
                            else -> "Watchlist"
                        }

                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(AppRoutes.TRENDING) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {},
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.TRENDING,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoutes.TRENDING) {
                TrendingScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(AppRoutes.details(movieId))
                    }
                )
            }

            composable(AppRoutes.SEARCH) {
                SearchScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(AppRoutes.details(movieId))
                    }
                )
            }

            composable(AppRoutes.WATCHLIST) {
                WatchlistScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(AppRoutes.details(movieId))
                    }
                )
            }

            composable(
                route = AppRoutes.DETAILS,
                arguments = listOf(
                    navArgument("movieId") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->
                val movieId =
                    entry.arguments?.getInt("movieId") ?: return@composable

                MovieDetailsScreen(
                    movieId = movieId,
                    onLogViewingClick = { selectedMovieId ->
                        navController.navigate(
                            AppRoutes.logViewing(selectedMovieId)
                        )
                    }
                )
            }

            composable(
                route = AppRoutes.LOG_VIEWING,
                arguments = listOf(
                    navArgument("movieId") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->
                val movieId =
                    entry.arguments?.getInt("movieId") ?: return@composable

                LogViewingScreen(
                    movieId = movieId,
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
