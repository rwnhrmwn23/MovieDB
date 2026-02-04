package com.onedev.moviedb.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val searchResults: Flow<PagingData<Movie>> = _query
        .debounce(500)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                emptyFlow()
            } else {
                searchMoviesUseCase(query).cachedIn(viewModelScope)
            }
        }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}
