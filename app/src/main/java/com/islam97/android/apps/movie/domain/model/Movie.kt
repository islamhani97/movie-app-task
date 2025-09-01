package com.islam97.android.apps.movie.domain.model

data class Movie(
    val id: Int,
    val title: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseYear: Int?,
    val overview: String?,
    val voteAverage: Double?,
    val genres: List<Genre>?
)