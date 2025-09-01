package com.islam97.android.apps.movie.data.dto

import com.google.gson.annotations.SerializedName
import com.islam97.android.apps.movie.domain.model.Genre

data class GenreDto(@SerializedName("id") val id: Int, @SerializedName("name") val name: String?)

fun GenreDto.toModel(): Genre {
    return Genre(id = id, name = name)
}