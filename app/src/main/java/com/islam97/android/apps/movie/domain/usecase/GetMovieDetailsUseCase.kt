package com.islam97.android.apps.movie.domain.usecase

import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase
@Inject constructor(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): Result<Movie> {
        return movieRepository.getMovieDetails(movieId)
    }
}