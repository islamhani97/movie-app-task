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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(private val getMovieListUseCase: GetMovieListUseCase) :
    MviViewModel<HomeState, HomeIntent, HomeEffect>() {

    override val mutableState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Idle)

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> loadMovies()
        }
    }

    private fun loadMovies() {
        mutableState.value =
            HomeState.Content(getMovieListUseCase.invoke().flow.cachedIn(viewModelScope))
    }
}

sealed interface HomeState {
    data object Idle : HomeState
    data object Loading : HomeState
    data class Content(val movies: Flow<PagingData<Movie>>) : HomeState
    data class Error(val message: String) : HomeState
}

sealed interface HomeIntent {
    data object Load : HomeIntent
}

sealed interface HomeEffect