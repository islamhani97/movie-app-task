package com.islam97.android.apps.movie.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.islam97.android.apps.movie.R
import com.islam97.android.apps.movie.domain.model.Movie

@Composable
fun MovieGrid(
    modifier: Modifier = Modifier, movies: LazyPagingItems<Movie>, onMovieClick: (Movie) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(movies.itemCount) { index ->
            movies[index]?.let { movie ->
                MovieItem(movie = movie, onClick = onMovieClick)
            }
        }

        movies.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        LoadingItem(message = stringResource(R.string.loading_movies))
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        LoadingItem(message = stringResource(R.string.loading_more))
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val e = loadState.refresh as LoadState.Error
                    item(span = { GridItemSpan(2) }) {
                        ErrorItem(
                            message = e.error.localizedMessage
                                ?: stringResource(R.string.error_something_went_wrong),
                            onRetry = { retry() })
                    }
                }

                loadState.append is LoadState.Error -> {
                    val e = loadState.append as LoadState.Error
                    item(span = { GridItemSpan(2) }) {
                        ErrorItem(
                            message = e.error.localizedMessage
                                ?: stringResource(R.string.error_something_went_wrong),
                            onRetry = { retry() })
                    }
                }
            }
        }
    }
}