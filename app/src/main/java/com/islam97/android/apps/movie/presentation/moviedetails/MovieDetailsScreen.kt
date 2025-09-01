package com.islam97.android.apps.movie.presentation.moviedetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class RouteMovieDetailsScreen(val movieId: Int)

@Composable
fun MovieDetailsScreen(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val route = backStackEntry.toRoute<RouteMovieDetailsScreen>()
        Text("Movie ID: ${route.movieId}")
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