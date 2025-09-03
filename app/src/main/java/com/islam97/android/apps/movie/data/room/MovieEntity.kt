package com.islam97.android.apps.movie.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String?,
    val posterUrl: String?,
    val releaseYear: Int?,
)