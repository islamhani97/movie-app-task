package com.islam97.android.apps.movie.presentation.moviedetails

import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.presentation.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject constructor() : MviViewModel<MovieDetailsState, MovieDetailsIntent, MovieDetailsEffect>() {

    override val mutableState: MutableStateFlow<MovieDetailsState> =
        MutableStateFlow(MovieDetailsState.Loading)

    override fun handleIntent(intent: MovieDetailsIntent) {
        when (intent) {
            MovieDetailsIntent.LoadMovieDetails -> loadMovieDetails()
        }
    }

    private fun loadMovieDetails() {
        //TODO: Load movie details from API
    }
}


sealed interface MovieDetailsState {
    data object Loading : MovieDetailsState
    data class Success(val movie: Movie) : MovieDetailsState
    data class Error(val message: String) : MovieDetailsState

}

sealed interface MovieDetailsIntent {
    data object LoadMovieDetails : MovieDetailsIntent
}

sealed interface MovieDetailsEffect