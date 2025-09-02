package com.islam97.android.apps.movie.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.islam97.android.apps.movie.presentation.home.components.MovieSearchBar
import com.islam97.android.apps.movie.presentation.moviedetails.RouteMovieDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data object RouteHomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }

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
        modifier = Modifier.fillMaxSize(), topBar = {
            MovieSearchBar(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    viewModel.handleIntent(HomeIntent.Search(it))
                },
                onSearch = {
                    viewModel.handleIntent(HomeIntent.Search(it))
                })
        }) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val (movieGridReference, loadingReference, errorReference) = createRefs()

            when (val uiState = state) {
                HomeState.Loading -> {
                    LoadingItem(modifier = Modifier.constrainAs(loadingReference) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }, message = stringResource(R.string.loading))
                }

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

                is HomeState.Error -> {
                    ErrorItem(
                        modifier = Modifier.constrainAs(errorReference) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                        message = uiState.errorMessage,
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