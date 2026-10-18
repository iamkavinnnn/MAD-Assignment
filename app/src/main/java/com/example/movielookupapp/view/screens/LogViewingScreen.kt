package com.example.movielookupapp.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielookupapp.model.local.Viewing
import com.example.movielookupapp.model.formatDate
import com.example.movielookupapp.model.todayText
import com.example.movielookupapp.view.theme.MovieLookupAppTheme
import kotlin.math.roundToInt

// Add a new viewing (existing = null) or edit one (existing = that viewing).
// The fields use rememberSaveable, so a half-filled form survives rotation (R1, Lecture 5).
// onSave returns null when saved, or a message to show (like addContact in Lecture 5).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogViewingScreen(
    existing: Viewing?,
    onSave: (dateText: String, rating: Int, note: String) -> String?,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var date by rememberSaveable {
        mutableStateOf(if (existing != null) formatDate(existing.watchedOn) else todayText())
    }
    var rating by rememberSaveable { mutableStateOf(existing?.rating ?: 3) }
    var notes by rememberSaveable { mutableStateOf(existing?.note ?: "") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(if (existing != null) "Edit Viewing" else "Log Viewing") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("Back") }
                }
            )
        }
    ) { innerPadding ->
        // verticalScroll keeps the Save button reachable in landscape and with large text (R5).
        // widthIn stops the fields stretching across a whole tablet (Lecture 4 Part 8).
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.widthIn(max = 480.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Record your movie viewing",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                        errorMessage = null
                    },
                    label = { Text("Date watched (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Your rating: $rating/5",
                    style = MaterialTheme.typography.titleMedium
                )

                Slider(
                    value = rating.toFloat(),
                    onValueChange = {
                        rating = it.roundToInt()
                        errorMessage = null
                    },
                    valueRange = 1f..5f,
                    steps = 3, // 1, 2, 3, 4, 5
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Rating, $rating out of 5" }
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = {
                        notes = it
                        errorMessage = null
                    },
                    label = { Text("Notes (optional)") },
                    supportingText = { Text("${notes.length}/200") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Button(
                    onClick = { errorMessage = onSave(date, rating, notes) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Viewing")
                }

                // Delete is quieter than Save (Lecture 4, Visibility), and only appears when editing.
                if (existing != null) {
                    TextButton(
                        onClick = onDelete,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete this viewing", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LogViewingScreenPreview() {
    MovieLookupAppTheme {
        LogViewingScreen(
            existing = null,
            onSave = { _, _, _ -> null },
            onDelete = {},
            onBackClick = {}
        )
    }
}
