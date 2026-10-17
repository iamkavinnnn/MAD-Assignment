
package com.example.movielookupapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.movielookupapp.ui.screens.TrendingScreen
import com.example.movielookupapp.ui.theme.MovieLookupAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MovieLookupAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TrendingScreen(
                        onMovieClick = { movieId ->
                            // Details navigation will be added later.
                        }
                    )
                }
            }
        }
    }
}
