package com.islam97.android.apps.movie.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMovieDetailsUseCaseTest {
    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke returns Success when repository succeeds`() = runTest {
        // Arrange
        val movie = Movie(
            id = 1,
            title = "Movie",
            posterUrl = "https://www.example.com/movie-poster.jpg",
            backdropUrl = "https://www.example.com/movie-backdrop.jpg",
            releaseYear = 2025,
            overview = "Movie",
            rating = 9.0,
            genres = listOf(),
        )
        coEvery { repository.getMovieDetails(1) } returns Result.Success(movie)

        // Act
        val result = useCase(1)

        // Assert
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data?.title).isEqualTo("Movie")
    }

    @Test
    fun `invoke returns Error when repository fails`() = runTest {
        // Arrange
        coEvery { repository.getMovieDetails(1) } returns Result.Error("Network error")

        // Act
        val result = useCase(1)

        // Assert
        assertThat(result is Result.Error).isTrue()
        assertThat((result as Result.Error).errorMessage).isEqualTo("Network error")
    }
}