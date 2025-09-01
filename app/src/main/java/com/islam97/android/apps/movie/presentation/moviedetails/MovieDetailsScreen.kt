package com.islam97.android.apps.movie.presentation.moviedetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class RouteMovieDetailsScreen(val movieId: Int)

@Composable
fun MovieDetailsScreen(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        val route = backStackEntry.toRoute<RouteMovieDetailsScreen>()
        viewModel.handleIntent(MovieDetailsIntent.LoadMovieDetails(route.movieId))
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val uiState = state) {
            is MovieDetailsState.Loading -> {
                Text("Loading ...")
            }

            is MovieDetailsState.Success -> {
                Text("Movie: ${uiState.movie.title}")
            }

            is MovieDetailsState.Error -> {
                Text("Error: ${uiState.errorMessage}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieDetailsScreen() {
    MovieDetailsScreen(
        navController = NavHostController(LocalContext.current),
        backStackEntry = NavHostController(LocalContext.current).currentBackStackEntry!!
    )
}