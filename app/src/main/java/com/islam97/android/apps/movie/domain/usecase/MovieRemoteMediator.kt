package com.islam97.android.apps.movie.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.data.dto.MovieListResponseDto
import com.islam97.android.apps.movie.data.mapper.toEntity
import com.islam97.android.apps.movie.data.room.AppDatabase
import com.islam97.android.apps.movie.data.room.MovieEntity

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    val apiCall: suspend (page: Int) -> Result<MovieListResponseDto>,
    private val database: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {

    private var lastKey = 1

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    lastKey = 1
                    1
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    lastKey + 1
                }
            }
            val response = apiCall(page)
            when (response) {
                is Result.Success -> {
                    response.data?.results?.map { it.toEntity() }?.let {
                        database.withTransaction {
                            if (loadType == LoadType.REFRESH) database.movieDao().clearAll()
                            database.movieDao().insertAll(it)
                        }
                        lastKey = response.data.page ?: 1
                        MediatorResult.Success(
                            endOfPaginationReached = (response.data.page
                                ?: 1) >= (response.data.totalPages ?: 1)
                        )
                    } ?: MediatorResult.Error(RuntimeException())
                }

                is Result.Error -> {
                    MediatorResult.Error(RuntimeException(response.errorMessage))
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
