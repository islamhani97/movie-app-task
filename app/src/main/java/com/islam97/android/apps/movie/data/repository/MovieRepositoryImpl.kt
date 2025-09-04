package com.islam97.android.apps.movie.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.islam97.android.apps.movie.core.utils.ApiCallHandler
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.data.mapper.toModel
import com.islam97.android.apps.movie.data.remote.MovieApi
import com.islam97.android.apps.movie.data.room.AppDatabase
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.model.MovieListResponse
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import com.islam97.android.apps.movie.domain.usecase.MovieRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl
@Inject constructor(
    private val apiCallHandler: ApiCallHandler,
    private val movieApi: MovieApi,
    private val database: AppDatabase
) : MovieRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovieList(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 1,
            remoteMediator = MovieRemoteMediator(
                apiCall = { page ->
                    apiCallHandler.callApi(apiCall = {
                        movieApi.getMovieList(page)
                    }) { this }
                }, database = database
            ),
            pagingSourceFactory = { database.movieDao().getMovieList() }).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }
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