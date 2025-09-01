package com.islam97.android.apps.movie.data.dto

import com.google.gson.annotations.SerializedName
import com.islam97.android.apps.movie.BuildConfig
import com.islam97.android.apps.movie.domain.model.Movie
import java.util.Calendar
import java.util.Date

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: Date?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("original_title") val originalTitle: String?
)

fun MovieDto.toModel(): Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = posterPath?.let { "${BuildConfig.IMAGE_BASE_URL}$it" },
        backdropUrl = backdropPath?.let { "${BuildConfig.IMAGE_BASE_URL}$it" },
        releaseYear = releaseDate?.let {
            Calendar.getInstance().apply { setTime(it) }[Calendar.YEAR]
        },
        overview = overview,
        voteAverage = String.format("%.1f", voteAverage).toDouble(),
        genres = genres?.map { it.toModel() })
}