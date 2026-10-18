package com.example.movielookupapp.view

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movielookupapp.view.screens.LogViewingScreen
import com.example.movielookupapp.view.screens.MovieDetailsScreen
import com.example.movielookupapp.view.screens.SearchScreen
import com.example.movielookupapp.view.screens.TrendingScreen
import com.example.movielookupapp.view.screens.WatchlistScreen
import com.example.movielookupapp.viewmodel.DetailsViewModel
import com.example.movielookupapp.viewmodel.SearchViewModel
import com.example.movielookupapp.viewmodel.TrendingViewModel
import com.example.movielookupapp.viewmodel.WatchlistViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon

// Where the ViewModels (Member A) meet the screens (Member B).
// - The ViewModels are created once, above the NavHost, so every screen shares them (Lecture 5).
// - Screens only get state values and plain lambdas, never a ViewModel or NavController
//   (Lecture 3 state hoisting, Lecture 6 "Notice what's missing").
// - This is the only file that knows about both sides.
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val trendingVm: TrendingViewModel = viewModel()
    val searchVm: SearchViewModel = viewModel()
    val detailsVm: DetailsViewModel = viewModel()
    val watchlistVm: WatchlistViewModel = viewModel()

    // Which screen is showing now. The bottom bar appears only on the three tab screens.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val onTrending = currentDestination?.hasRoute<TrendingRoute>() == true
    val onSearch = currentDestination?.hasRoute<SearchRoute>() == true
    val onWatchlist = currentDestination?.hasRoute<WatchlistRoute>() == true
    val showBottomBar = onTrending || onSearch || onWatchlist

    // Moves to a tab, keeping one copy of each tab and remembering where each tab was.
    fun openTab(route: Any) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                // Text labels only so there are no icon-only controls to describe.
                // Navigation Bar
                // reference: https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-navigation-bar.html
                // reference: https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-navigation-bar-item.html
                NavigationBar {
                    NavigationBarItem(
                        selected = onTrending,
                        onClick = { openTab(TrendingRoute) },
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text("Trending") }
                    )
                    NavigationBarItem(
                        selected = onSearch,
                        onClick = { openTab(SearchRoute) },
                        icon = { Icon(Icons.Default.Search, contentDescription = null) },
                        label = { Text("Search") }
                    )
                    NavigationBarItem(
                        selected = onWatchlist,
                        onClick = { openTab(WatchlistRoute) },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        label = { Text("Watchlist") }
                    )
                }
            }
            }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TrendingRoute,
            // consumeWindowInsets stops each screen's own Scaffold adding the
            // status-bar / navigation-bar space a second time.
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {

            composable<TrendingRoute> {
                TrendingScreen(
                    state = trendingVm.state,
                    onRetry = { trendingVm.retry() },
                    onMovieClick = { id -> navController.navigate(DetailsRoute(id)) }
                )
            }

            composable<SearchRoute> {
                SearchScreen(
                    query = searchVm.query,
                    state = searchVm.state,
                    onQueryChange = { searchVm.onQueryChange(it) },
                    onSearch = { searchVm.search() },
                    onRetry = { searchVm.retry() },
                    onMovieClick = { id -> navController.navigate(DetailsRoute(id)) }
                )
            }

            composable<WatchlistRoute> {
                WatchlistScreen(
                    entries = watchlistVm.entries,
                    sortOrder = watchlistVm.sortOrder,
                    onSortChange = { order -> watchlistVm.changeSortOrder(order) },
                    onMovieClick = { id -> navController.navigate(DetailsRoute(id)) },
                    onLogViewing = { id -> navController.navigate(LogViewingRoute(movieId = id)) },
                    onRemove = { item -> watchlistVm.remove(item) }
                )
            }

            composable<DetailsRoute> { entry ->
                val route: DetailsRoute = entry.toRoute()

                // Ask the ViewModel to load this movie when the screen opens.
                // LaunchedEffect is not in Lectures 1–7 (Lecture 3 names it as the tool for this).
                LaunchedEffect(route.movieId) {
                    detailsVm.load(route.movieId)
                }

                MovieDetailsScreen(
                    state = detailsVm.state,
                    isSaved = watchlistVm.isSaved(route.movieId),
                    viewings = watchlistVm.viewingsFor(route.movieId),
                    onRetry = { detailsVm.retry() },
                    onAddToWatchlist = { movie -> watchlistVm.add(movie) },
                    onShare = { movie -> shareMovie(context, movie) },
                    onLogViewing = { navController.navigate(LogViewingRoute(movieId = route.movieId)) },
                    onEditViewing = { viewingId ->
                        navController.navigate(LogViewingRoute(movieId = route.movieId, viewingId = viewingId))
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable<LogViewingRoute> { entry ->
                val route: LogViewingRoute = entry.toRoute()
                val existing = watchlistVm.findViewing(route.viewingId)
                LogViewingScreen(
                    existing = existing,
                    // Returns null when saved, or a message for the screen to show (Lecture 3,
                    // handlers that return a value; Lecture 5, addContact returning a result).
                    onSave = { dateText, rating, note ->
                        val error = watchlistVm.saveViewing(route.movieId, route.viewingId, dateText, rating, note)
                        if (error == null) navController.popBackStack()
                        error
                    },
                    // Only shown when editing an existing viewing.
                    onDelete = {
                        if (existing != null) watchlistVm.deleteViewing(existing)
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
