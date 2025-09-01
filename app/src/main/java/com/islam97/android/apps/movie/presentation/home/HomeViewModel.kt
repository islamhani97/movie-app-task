package com.islam97.android.apps.movie.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.usecase.GetMovieListUseCase
import com.islam97.android.apps.movie.presentation.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(private val getMovieListUseCase: GetMovieListUseCase) :
    MviViewModel<HomeState, HomeIntent, HomeEffect>() {

    override val mutableState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)

    init {
        handleIntent(HomeIntent.LoadMovies)
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadMovies -> loadMovies()
            is HomeIntent.NavigateToDetailsScreen -> viewModelScope.launch {
                mutableEffectFlow.emit(HomeEffect.NavigateToDetailsScreen(movieId = intent.movieId))
            }
        }
    }

    private fun loadMovies() {
        mutableState.value =
            HomeState.Content(getMovieListUseCase.invoke().flow.cachedIn(viewModelScope))
    }
}

sealed interface HomeState {
    data object Loading : HomeState
    data class Content(val movies: Flow<PagingData<Movie>>) : HomeState
    data class Error(val message: String) : HomeState
}

sealed interface HomeIntent {
    data object LoadMovies : HomeIntent
    data class NavigateToDetailsScreen(val movieId: Int) : HomeIntent
}

sealed interface HomeEffect {
    data class NavigateToDetailsScreen(val movieId: Int) : HomeEffect
}