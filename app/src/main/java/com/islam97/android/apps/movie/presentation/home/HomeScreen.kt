package com.islam97.android.apps.movie.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.islam97.android.apps.movie.R
import com.islam97.android.apps.movie.presentation.home.components.ErrorItem
import com.islam97.android.apps.movie.presentation.home.components.LoadingItem
import com.islam97.android.apps.movie.presentation.home.components.MovieGrid
import com.islam97.android.apps.movie.presentation.moviedetails.RouteMovieDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data object RouteHomeScreen

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect {
            when (it) {
                is HomeEffect.NavigateToDetailsScreen -> {
                    navController.navigate(RouteMovieDetailsScreen(it.movieId))
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val (movieGridReference, loadingReference, errorReference) = createRefs()
            when (val uiState = state) {
                is HomeState.Content -> {
                    val movies = uiState.movies.collectAsLazyPagingItems()
                    MovieGrid(modifier = Modifier.constrainAs(movieGridReference) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }, movies = movies, onMovieClick = {
                        viewModel.handleIntent(
                            HomeIntent.NavigateToDetailsScreen(movieId = it.id)
                        )
                    })
                }

                HomeState.Loading -> {
                    LoadingItem(modifier = Modifier.constrainAs(loadingReference) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }, message = stringResource(R.string.loading_movies))
                }

                is HomeState.Error -> {
                    ErrorItem(
                        modifier = Modifier.constrainAs(errorReference) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                        message = uiState.message,
                        onRetry = { viewModel.handleIntent(HomeIntent.LoadMovies) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}