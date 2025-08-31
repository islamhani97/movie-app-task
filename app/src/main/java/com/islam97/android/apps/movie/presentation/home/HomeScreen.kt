package com.islam97.android.apps.movie.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.serialization.Serializable

@Serializable
object RouteHomeScreen

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(HomeIntent.Load)
    }

    when (val uiState = state) {
        HomeState.Idle -> { /* show nothing */
        }

        HomeState.Loading -> {
            CircularProgressIndicator()
        }

        is HomeState.Content -> {
            val movies = uiState.movies.collectAsLazyPagingItems()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies.itemCount) { index ->
                    movies[index]?.let { movie ->
                        Text(text = movie.title ?: "")
                    }
                }

                when (movies.loadState.append) {
                    is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    is LoadState.Error -> {
                        item { Text("Error loading more") }
                    }

                    else -> {}
                }
            }
        }

        is HomeState.Error -> {
            Text(text = uiState.message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}