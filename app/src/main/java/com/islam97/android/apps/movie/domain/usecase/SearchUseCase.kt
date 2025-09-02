package com.islam97.android.apps.movie.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import javax.inject.Inject

class SearchUseCase
@Inject constructor(private val movieRepository: MovieRepository) {
    fun invoke(searchQuery: String): Pager<Int, Movie> {
        return Pager(config = PagingConfig(pageSize = 20), initialKey = 1, pagingSourceFactory = {
            MoviePagingSource { page -> movieRepository.search(searchQuery, page) }
        })
    }
}