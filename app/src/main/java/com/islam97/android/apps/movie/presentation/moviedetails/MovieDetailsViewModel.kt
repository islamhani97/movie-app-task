package com.islam97.android.apps.movie.presentation.moviedetails

import androidx.lifecycle.viewModelScope
import com.islam97.android.apps.movie.core.utils.Result
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.usecase.GetMovieDetailsUseCase
import com.islam97.android.apps.movie.presentation.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject constructor(private val getMovieDetailsUseCase: GetMovieDetailsUseCase) :
    MviViewModel<MovieDetailsState, MovieDetailsIntent, MovieDetailsEffect>() {

    override val mutableState: MutableStateFlow<MovieDetailsState> =
        MutableStateFlow(MovieDetailsState.Loading)

    override fun handleIntent(intent: MovieDetailsIntent) {
        when (intent) {
            is MovieDetailsIntent.LoadMovieDetails -> {
                mutableState.value = MovieDetailsState.Loading
                loadMovieDetails(movieId = intent.movieId)
            }

            MovieDetailsIntent.Back -> viewModelScope.launch {
                mutableEffectFlow.emit(MovieDetailsEffect.Back)
            }
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getMovieDetailsUseCase.invoke(movieId = movieId)) {
                is Result.Success -> {
                    mutableState.value = MovieDetailsState.Success(result.data!!)
                }

                is Result.Error -> {
                    mutableState.value = MovieDetailsState.Error(result.errorMessage)
                }
            }
        }
    }
}

sealed interface MovieDetailsState {
    data object Loading : MovieDetailsState
    data class Success(val movie: Movie) : MovieDetailsState
    data class Error(val errorMessage: String) : MovieDetailsState
}

sealed interface MovieDetailsIntent {
    data class LoadMovieDetails(val movieId: Int) : MovieDetailsIntent
    data object Back : MovieDetailsIntent
}

sealed interface MovieDetailsEffect {
    data object Back : MovieDetailsEffect
}