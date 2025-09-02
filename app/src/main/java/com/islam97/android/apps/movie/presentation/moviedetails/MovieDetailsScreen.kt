package com.islam97.android.apps.movie.presentation.moviedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import com.islam97.android.apps.movie.R
import com.islam97.android.apps.movie.presentation.home.components.ErrorItem
import com.islam97.android.apps.movie.presentation.home.components.LoadingItem
import kotlinx.serialization.Serializable

@Serializable
data class RouteMovieDetailsScreen(val movieId: Int)

@OptIn(ExperimentalMaterial3Api::class)
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

    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect {
            when (it) {
                is MovieDetailsEffect.Back -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                if (state is MovieDetailsState.Success) {
                    Text((state as MovieDetailsState.Success).movie.title ?: "")
                }
            }, navigationIcon = {
                IconButton(onClick = { viewModel.handleIntent(MovieDetailsIntent.Back) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        }) { innerPadding ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            val (backdropReference, posterReference, movieDetailsReference, overviewLabelReference, overviewReference, loadingReference, errorReference) = createRefs()
            val startGuideline = createGuidelineFromStart(0.05f)
            val endGuideline = createGuidelineFromEnd(0.05f)
            val overviewBarrier = createBottomBarrier(posterReference, movieDetailsReference)

            when (val uiState = state) {
                is MovieDetailsState.Loading -> {
                    LoadingItem(modifier = Modifier.constrainAs(loadingReference) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }, message = stringResource(R.string.loading))
                }

                is MovieDetailsState.Success -> {
                    AsyncImage(
                        modifier = Modifier
                            .constrainAs(backdropReference) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        model = uiState.movie.backdropUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop)

                    AsyncImage(
                        modifier = Modifier
                            .constrainAs(posterReference) {
                                top.linkTo(backdropReference.bottom)
                                start.linkTo(startGuideline)
                            }
                            .width(120.dp)
                            .aspectRatio(2f / 3f)
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        model = uiState.movie.posterUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop)

                    Column(
                        modifier = Modifier
                            .constrainAs(movieDetailsReference) {
                                top.linkTo(posterReference.top)
                                start.linkTo(posterReference.end)
                                end.linkTo(endGuideline)
                                width = Dimension.fillToConstraints
                            }
                            .padding(top = 16.dp, start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = uiState.movie.title ?: "",
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        uiState.movie.releaseYear?.let {
                            Text(
                                text = "$it",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        uiState.movie.rating?.let {
                            Text(
                                text = "⭐ $it",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            uiState.movie.genres?.forEach { genre ->
                                AssistChip(onClick = {}, label = { Text(genre.name ?: "") })
                            }
                        }
                    }

                    Text(
                        modifier = Modifier
                            .constrainAs(overviewLabelReference) {
                                top.linkTo(overviewBarrier)
                                start.linkTo(startGuideline)
                            }
                            .padding(top = 16.dp),
                        text = stringResource(R.string.overview),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(overviewReference) {
                                top.linkTo(overviewLabelReference.bottom)
                                start.linkTo(startGuideline)
                                end.linkTo(endGuideline)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                                verticalBias = 0f
                            }
                            .padding(vertical = 16.dp),
                        text = uiState.movie.overview ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                is MovieDetailsState.Error -> {
                    ErrorItem(
                        modifier = Modifier.constrainAs(errorReference) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                        message = uiState.errorMessage,
                        onRetry = {
                            val route = backStackEntry.toRoute<RouteMovieDetailsScreen>()
                            viewModel.handleIntent(MovieDetailsIntent.LoadMovieDetails(route.movieId))
                        })
                }
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