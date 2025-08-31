package com.islam97.android.apps.movie.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieListUseCase
@Inject constructor(private val movieRepository: MovieRepository) {
    fun invoke(): Pager<Int, Movie> {
        return Pager(config = PagingConfig(pageSize = 20), initialKey = 1) {
            object : PagingSource<Int, Movie>() {
                override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
                    return state.anchorPosition?.let { anchorPosition ->
                        val anchorPage = state.closestPageToPosition(anchorPosition)
                        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                    }
                }

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
                    return try {
                        val currentPage = params.key ?: 1
                        val response = movieRepository.getMovieList(currentPage)
                        when (response) {
                            is Result.Success -> {
                                val nextPage = if (currentPage >= (response.data?.totalPages
                                        ?: currentPage)
                                ) null else currentPage + 1
                                val previousPage = if (currentPage == 1) null else currentPage - 1
                                LoadResult.Page(
                                    data = response.data?.movies ?: listOf(),
                                    prevKey = previousPage,
                                    nextKey = nextPage
                                )
                            }

                            is Result.Error -> {
                                LoadResult.Error(RuntimeException(response.errorMessage))
                            }
                        }
                    } catch (e: Exception) {
                        LoadResult.Error(e)
                    }
                }
            }
        }
    }
}