package com.islam97.android.apps.movie.domain.usecase

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.common.truth.Truth.assertThat
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMovieListUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetMovieListUseCase
    private lateinit var diffCallback: DiffUtil.ItemCallback<Movie>
    private lateinit var listUpdateCallback: ListUpdateCallback

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = GetMovieListUseCase(repository)
        diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
        }
        listUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
            }

            override fun onRemoved(position: Int, count: Int) {
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
            }

            override fun onChanged(
                position: Int,
                count: Int,
                payload: Any?
            ) {
            }
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        // Arrange
        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie1",
                posterUrl = "https://www.example.com/movie-poster1.jpg",
                backdropUrl = "https://www.example.com/movie-backdrop1.jpg",
                releaseYear = 2025,
                overview = "Movie1",
                rating = 9.0,
                genres = listOf(),
            ),
            Movie(
                id = 2,
                title = "Movie2",
                posterUrl = "https://www.example.com/movie-poster2.jpg",
                backdropUrl = "https://www.example.com/movie-backdrop2.jpg",
                releaseYear = 2025,
                overview = "Movie2",
                rating = 9.0,
                genres = listOf(),
            )
        )
        val pagingData = PagingData.from(movies)
        coEvery { repository.getMovieList() } returns flowOf(pagingData)

        // Act
        val resultFlow = useCase.invoke()
        val result = resultFlow.first()

        // Assert
        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
            },
            updateCallback = object : ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {
                }

                override fun onRemoved(position: Int, count: Int) {
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                }

                override fun onChanged(
                    position: Int,
                    count: Int,
                    payload: Any?
                ) {
                }
            },
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.IO
        )

        differ.submitData(result)

        val snapshot = differ.snapshot()
        assertThat(snapshot.size).isEqualTo(2)
        assertThat(snapshot[0]?.title).isEqualTo("Movie1")
        assertThat(snapshot[1]?.title).isEqualTo("Movie2")
        coVerify(exactly = 1) { repository.getMovieList() }
    }
}