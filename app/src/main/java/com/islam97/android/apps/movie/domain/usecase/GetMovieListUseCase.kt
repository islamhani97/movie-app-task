package com.islam97.android.apps.movie.domain.usecase

import androidx.paging.PagingData
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListUseCase
@Inject constructor(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(): Flow<PagingData<Movie>> {
        return movieRepository.getMovieList()
    }
}