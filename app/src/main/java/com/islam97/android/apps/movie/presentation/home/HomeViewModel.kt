package com.islam97.android.apps.movie.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.islam97.android.apps.movie.domain.model.Movie
import com.islam97.android.apps.movie.domain.usecase.GetMovieListUseCase
import com.islam97.android.apps.movie.domain.usecase.SearchUseCase
import com.islam97.android.apps.movie.presentation.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase, private val searchUseCase: SearchUseCase
) : MviViewModel<HomeState, HomeIntent, HomeEffect>() {

    override val mutableState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    private val searchFlow = MutableStateFlow("")

    init {
        handleIntent(HomeIntent.LoadMovies)
        observeSearch()
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadMovies -> {
                mutableState.value = HomeState.Loading
                loadMovies()
            }

            is HomeIntent.NavigateToDetailsScreen -> viewModelScope.launch {
                mutableEffectFlow.emit(HomeEffect.NavigateToDetailsScreen(movieId = intent.movieId))
            }

            is HomeIntent.Search -> viewModelScope.launch {
                val searchQuery = intent.searchQuery.trim()
                if (searchQuery.length < 3) {
                    searchFlow.emit("")
                } else {
                    searchFlow.emit(searchQuery)
                }
            }
        }
    }

    private fun loadMovies() {
        mutableState.value =
            HomeState.Content(getMovieListUseCase.invoke().flow.cachedIn(viewModelScope))
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchFlow.debounce(500).distinctUntilChanged().collectLatest { query ->
                mutableState.value = HomeState.Loading
                if (query.isBlank()) {
                    loadMovies()
                } else {
                    search(query)
                }
            }
        }
    }

    private fun search(searchQuery: String) {
        mutableState.value =
            HomeState.Content(searchUseCase.invoke(searchQuery).flow.cachedIn(viewModelScope))
    }
}

sealed interface HomeState {
    data object Loading : HomeState
    data class Content(val movies: Flow<PagingData<Movie>>) : HomeState
    data class Error(val errorMessage: String) : HomeState
}

sealed interface HomeIntent {
    data object LoadMovies : HomeIntent
    data class NavigateToDetailsScreen(val movieId: Int) : HomeIntent
    data class Search(val searchQuery: String) : HomeIntent
}

sealed interface HomeEffect {
    data class NavigateToDetailsScreen(val movieId: Int) : HomeEffect
}