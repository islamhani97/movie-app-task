package com.islam97.android.apps.movie.data.mapper

import com.islam97.android.apps.movie.BuildConfig
import com.islam97.android.apps.movie.core.utils.getReleaseYear
import com.islam97.android.apps.movie.data.dto.MovieDto
import com.islam97.android.apps.movie.data.room.MovieEntity
import com.islam97.android.apps.movie.domain.model.Movie
import java.util.Locale

fun MovieDto.toModel(): Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = posterPath?.let { "${BuildConfig.IMAGE_BASE_URL}$it" },
        backdropUrl = backdropPath?.let { "${BuildConfig.IMAGE_BASE_URL}$it" },
        releaseYear = releaseDate?.getReleaseYear(),
        overview = overview,
        rating = String.format(Locale.ENGLISH, "%.1f", voteAverage).toDouble(),
        genres = genres?.map { it.toModel() })
}

fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        posterUrl = posterPath?.let { "${BuildConfig.IMAGE_BASE_URL}$it" },
        releaseYear = releaseDate?.getReleaseYear()
    )
}

fun MovieEntity.toModel(): Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = posterUrl,
        backdropUrl = null,
        releaseYear = releaseYear,
        overview = null,
        rating = null,
        genres = null
    )
}