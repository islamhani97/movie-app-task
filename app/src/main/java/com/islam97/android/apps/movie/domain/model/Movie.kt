package com.islam97.android.apps.movie.domain.model

data class Movie(
    val id: Int,
    val title: String?,
    val posterUrl: String?,
    val releaseYear: Int?
)