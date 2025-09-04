package com.islam97.android.apps.movie.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.usecase.GetMovieDetailsUseCase
import com.islam97.android.apps.movie.presentation.moviedetails.MovieDetailsEffect
import com.islam97.android.apps.movie.presentation.moviedetails.MovieDetailsIntent
import com.islam97.android.apps.movie.presentation.moviedetails.MovieDetailsState
import com.islam97.android.apps.movie.presentation.moviedetails.MovieDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieDetailsViewModelTest {
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setup() {
        getMovieDetailsUseCase = mockk()
        viewModel = MovieDetailsViewModel(getMovieDetailsUseCase)
    }

    @Test
    fun `test load movie details intent success`() = runTest {
        // Arrange
        coEvery { getMovieDetailsUseCase(1) } returns Result.Success(
            data = Movie(
                id = 1,
                title = "Movie1",
                posterUrl = "https://www.example.com/movie-poster1.jpg",
                backdropUrl = "https://www.example.com/movie-backdrop1.jpg",
                releaseYear = 2025,
                overview = "Movie1",
                rating = 9.0,
                genres = listOf(),
            )
        )

        viewModel.state.test {
            // Act
            viewModel.handleIntent(MovieDetailsIntent.LoadMovieDetails(1))

            // Assert
            assertThat(awaitItem() is MovieDetailsState.Loading).isTrue()
            assertThat(awaitItem() is MovieDetailsState.Success).isTrue()
        }
    }

    @Test
    fun `test load movie details intent with fail`() = runTest {
        // Arrange
        coEvery { getMovieDetailsUseCase(1) } returns Result.Error("Error")

        viewModel.state.test {
            // Act
            viewModel.handleIntent(MovieDetailsIntent.LoadMovieDetails(1))

            // Assert
            assertThat(awaitItem() is MovieDetailsState.Loading).isTrue()
            assertThat(awaitItem() is MovieDetailsState.Error).isTrue()
        }
    }

    @Test
    fun `test Back intent`() = runTest {
        // Arrange
        val intent = MovieDetailsIntent.Back

        viewModel.effectFlow.test {
            // Act
            viewModel.handleIntent(intent)

            // Assert
            assertThat(awaitItem() is MovieDetailsEffect.Back).isTrue()
        }
    }
}