package com.islam97.android.apps.movie.domain.repository

import androidx.paging.PagingData
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.model.MovieListResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieList(): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    suspend fun search(searchQuery: String, page: Int): Result<MovieListResponse>
}