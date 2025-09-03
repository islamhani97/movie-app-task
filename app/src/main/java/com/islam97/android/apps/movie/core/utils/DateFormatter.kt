package com.islam97.android.apps.movie.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

fun String.getReleaseYear(): Int? {
    return try {
        DATE_FORMAT.parse(this)?.let { date ->
            Calendar.getInstance().apply {
                setTime(date)
            }[Calendar.YEAR]
        }
    } catch (_: Exception) {
        null
    }
}