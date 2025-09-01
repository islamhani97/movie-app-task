package com.islam97.android.apps.movie.domain.model

data class MovieListResponse(
    val page: Int,
    val movies: List<Movie>,
    val totalPages: Int,
)