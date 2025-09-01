package com.islam97.android.apps.movie.data.dto

import com.google.gson.annotations.SerializedName
import com.islam97.android.apps.movie.domain.model.MovieListResponse

data class MovieListResponseDto(
    @SerializedName("page") val page: Int?,
    @SerializedName("results") val results: List<MovieDto>?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int
)

fun MovieListResponseDto.toModel(): MovieListResponse {
    return MovieListResponse(
        page = page ?: 1,
        movies = results?.map { it.toModel() } ?: listOf(),
        totalPages = totalPages ?: 1)
}