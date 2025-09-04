package com.islam97.android.apps.movie.data.room

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {
    private lateinit var context: Context
    private lateinit var database: AppDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        database =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
                .build()
        dao = database.movieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetMovies() = runTest {
        // Arrange
        val movie = MovieEntity(
            id = 1,
            title = "Movie",
            posterUrl = "https://www.example.com/movie-poster.jpg",
            releaseYear = 2025
        )

        // Act
        dao.insertAll(listOf(movie))
        val pagingSource = dao.getMovieList()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null, loadSize = 10, placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        // Assert
        assertThat(loadResult.data.size).isEqualTo(1)
        assertThat(loadResult.data[0].title).isEqualTo("Movie")
    }

    @Test
    fun clearAllMovies() = runTest {
        // Arrange
        val movie = MovieEntity(
            id = 1,
            title = "Movie",
            posterUrl = "https://www.example.com/movie-poster.jpg",
            releaseYear = 2025
        )
        dao.insertAll(listOf(movie))

        // Act
        dao.clearAll()
        val pagingSource = dao.getMovieList()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null, loadSize = 10, placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        // Assert
        assertThat(loadResult.data.isEmpty()).isTrue()
    }
}