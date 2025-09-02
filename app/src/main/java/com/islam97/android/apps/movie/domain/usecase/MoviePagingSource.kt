package com.islam97.android.apps.movie.domain.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.model.MovieListResponse

class MoviePagingSource(val apiCall: suspend (page: Int) -> Result<MovieListResponse>) :
    PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiCall(currentPage)
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