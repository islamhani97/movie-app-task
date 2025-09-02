package com.islam97.android.apps.movie.domain.repository

import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.model.MovieListResponse

interface MovieRepository {
    suspend fun getMovieList(page: Int): Result<MovieListResponse>
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    suspend fun search(searchQuery: String, page: Int): Result<MovieListResponse>
}