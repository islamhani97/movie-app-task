package com.islam97.android.apps.movie.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.usecase.GetMovieListUseCase
import com.islam97.android.apps.movie.domain.usecase.SearchUseCase
import com.islam97.android.apps.movie.presentation.home.HomeEffect
import com.islam97.android.apps.movie.presentation.home.HomeIntent
import com.islam97.android.apps.movie.presentation.home.HomeState
import com.islam97.android.apps.movie.presentation.home.HomeViewModel
import io.mockk.coEvery
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
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getMovieListUseCase: GetMovieListUseCase
    private lateinit var searchUseCase: SearchUseCase
    private lateinit var viewModel: HomeViewModel
    private lateinit var diffCallback: DiffUtil.ItemCallback<Movie>
    private lateinit var listUpdateCallback: ListUpdateCallback

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getMovieListUseCase = mockk(relaxed = true)
        searchUseCase = mockk(relaxed = true)
        viewModel = HomeViewModel(getMovieListUseCase, searchUseCase)
        diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

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
                position: Int, count: Int, payload: Any?
            ) {
            }
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test LoadMovies intent sets state to Content`() = runTest {
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
            ), Movie(
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

        coEvery { getMovieListUseCase.invoke() } returns flowOf(PagingData.from(movies))
        viewModel.state.test {
            //Act
            viewModel.handleIntent(HomeIntent.LoadMovies)

            // Assert
            skipItems(1)
            assertThat(awaitItem() is HomeState.Loading).isTrue()
            val content = awaitItem()
            assertThat(content is HomeState.Content).isTrue()
            val differ = AsyncPagingDataDiffer(
                diffCallback = diffCallback,
                updateCallback = listUpdateCallback,
                mainDispatcher = StandardTestDispatcher(),
                workerDispatcher = StandardTestDispatcher()
            )
            differ.submitData((content as HomeState.Content).movies.first())
            val snapshot = differ.snapshot()
            assertThat(snapshot.size).isEqualTo(2)
            assertThat(snapshot[0]?.title).isEqualTo("Movie1")
            assertThat(snapshot[1]?.title).isEqualTo("Movie2")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test Search with valid query sets state to Content`() = runTest {
        // Arrange
        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie",
                posterUrl = "https://www.example.com/movie-poster.jpg",
                backdropUrl = "https://www.example.com/movie-backdrop.jpg",
                releaseYear = 2025,
                overview = "Movie",
                rating = 9.0,
                genres = listOf(),
            )
        )
        coEvery { searchUseCase.invoke("Movie") } returns Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { FakeMoviePagingSource(movies) })

        viewModel.state.test {
            //Act
            viewModel.handleIntent(HomeIntent.Search("Movie"))

            // Assert
            assertThat(awaitItem() is HomeState.Loading).isTrue()
            val content = awaitItem()
            assertThat(content is HomeState.Content).isTrue()
        }
    }

    @Test
    fun `test NavigateToDetailsScreen intent`() = runTest {
        // Arrange
        val intent = HomeIntent.NavigateToDetailsScreen(movieId = 1)

        viewModel.effectFlow.test {
            // Act
            viewModel.handleIntent(intent)

            // Assert
            val effect = awaitItem()
            assertThat(effect is HomeEffect.NavigateToDetailsScreen).isTrue()
            assertThat((effect as HomeEffect.NavigateToDetailsScreen).movieId).isEqualTo(1)
        }
    }
}

class FakeMoviePagingSource(private val movies: List<Movie>) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>) = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return LoadResult.Page(movies, prevKey = null, nextKey = null)
    }
}