package com.islam97.android.apps.movie.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.islam97.android.apps.movie.core.utils.NAVIGATION_DURATION
import com.islam97.android.apps.movie.presentation.home.HomeScreen
import com.islam97.android.apps.movie.presentation.home.RouteHomeScreen
import com.islam97.android.apps.movie.presentation.moviedetails.MovieDetailsScreen
import com.islam97.android.apps.movie.presentation.moviedetails.RouteMovieDetailsScreen
import com.islam97.android.apps.movie.presentation.ui.theme.MovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = RouteHomeScreen,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }, animationSpec = tween(NAVIGATION_DURATION)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it }, animationSpec = tween(NAVIGATION_DURATION)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }, animationSpec = tween(NAVIGATION_DURATION)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }, animationSpec = tween(NAVIGATION_DURATION)
            )
        }) {
        composable<RouteHomeScreen> {
            HomeScreen(navController = navController)
        }

        composable<RouteMovieDetailsScreen> {
            MovieDetailsScreen(navController = navController, backStackEntry = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    MovieTheme {
        MainNavigation()
    }
}