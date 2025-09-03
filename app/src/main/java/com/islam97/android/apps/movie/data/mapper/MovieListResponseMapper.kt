package com.islam97.android.apps.movie.data.mapper

import com.islam97.android.apps.movie.data.dto.MovieListResponseDto
import com.islam97.android.apps.movie.domain.model.MovieListResponse

fun MovieListResponseDto.toModel(): MovieListResponse {
    return MovieListResponse(
        page = page ?: 1,
        movies = results?.map { it.toModel() } ?: listOf(),
        totalPages = totalPages ?: 1)
}