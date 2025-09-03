package com.islam97.android.apps.movie.data.mapper

import com.islam97.android.apps.movie.data.dto.GenreDto
import com.islam97.android.apps.movie.domain.model.Genre

fun GenreDto.toModel(): Genre {
    return Genre(id = id, name = name)
}