package com.islam97.android.apps.movie.data.repository

import com.islam97.android.apps.movie.core.utils.ApiCallHandler
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.data.dto.toModel
import com.islam97.android.apps.movie.data.remote.MovieApi
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.model.MovieListResponse
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl
@Inject constructor(
    private val apiCallHandler: ApiCallHandler, private val movieApi: MovieApi
) : MovieRepository {
    override suspend fun getMovieList(page: Int): Result<MovieListResponse> {
        return apiCallHandler.callApi(apiCall = { movieApi.getMovieList(page) }) { toModel() }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return apiCallHandler.callApi(apiCall = { movieApi.getMovieDetails(movieId) }) { toModel() }
    }

    override suspend fun search(
        searchQuery: String, page: Int
    ): Result<MovieListResponse> {
        return apiCallHandler.callApi(apiCall = {
            movieApi.search(
                searchQuery, page
            )
        }) { toModel() }
    }
}