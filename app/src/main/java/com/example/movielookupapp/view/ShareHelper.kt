package com.example.movielookupapp.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.movielookupapp.model.Movie
import com.example.movielookupapp.model.shareText

//  hands the movie to another app (Messages, WhatsApp, email...) with a share intent.
// - No runtime permission is needed for sharing.
// - Cancelling the chooser just returns to the app and nothing else happens.
// - If no app can receive it, we show a short message instead of crashing.
fun shareMovie(context: Context, movie: Movie) {
    val sendIntent = Intent(Intent.ACTION_SEND)
    sendIntent.type = "text/plain"
    sendIntent.putExtra(Intent.EXTRA_TEXT, movie.shareText())

    val chooser = Intent.createChooser(sendIntent, "Share ${movie.title}")
    try {
        context.startActivity(chooser)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No app available to share with.", Toast.LENGTH_SHORT).show()
    }
}
