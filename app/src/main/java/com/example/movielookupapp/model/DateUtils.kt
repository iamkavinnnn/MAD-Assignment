package com.example.movielookupapp.model

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Dates are typed as text in the Log Viewing form, e.g. "2026-10-18".
private const val DATE_PATTERN = "yyyy-MM-dd"

// Returns the date in milliseconds, or null if the text is not a real date.
// isLenient = false rejects dates like 2026-02-31.
// The ParsePosition check rejects extra characters, like "2026-10-18abc".
fun parseDate(text: String): Long? {
    val trimmed = text.trim()
    val format = SimpleDateFormat(DATE_PATTERN, Locale.US)
    format.isLenient = false
    val position = ParsePosition(0)
    val date = format.parse(trimmed, position)
    return if (date == null || position.index != trimmed.length) null else date.time
}

fun formatDate(millis: Long): String =
    SimpleDateFormat(DATE_PATTERN, Locale.US).format(Date(millis))

// Starting value for the date field in the form.
fun todayText(): String = formatDate(System.currentTimeMillis())
