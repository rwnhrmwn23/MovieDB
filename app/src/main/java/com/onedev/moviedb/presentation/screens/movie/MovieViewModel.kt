package com.onedev.moviedb.presentation.screens.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.Genre
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.usecase.GetGenresUseCase
import com.onedev.moviedb.domain.usecase.GetMoviesByGenreUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MovieViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
) : ViewModel() {

    private val _genres = MutableStateFlow<Resource<List<Genre>>>(Resource.Loading())
    val genres: StateFlow<Resource<List<Genre>>> = _genres.asStateFlow()

    private val _selectedGenreIds = MutableStateFlow<List<Int>>(emptyList())
    val selectedGenreIds: StateFlow<List<Int>> = _selectedGenreIds.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies: Flow<PagingData<Movie>> = _selectedGenreIds.flatMapLatest { genreIds ->
        if (genreIds.isEmpty()) {
            emptyFlow()
        } else {
            getMoviesByGenreUseCase(genreIds).cachedIn(viewModelScope)
        }
    }

    init {
        loadGenres()
    }

    private fun loadGenres() {
        viewModelScope.launch {
            _genres.value = Resource.Loading()
            _genres.value = getGenresUseCase()
            if (_genres.value is Resource.Success) {
                val list = (_genres.value as Resource.Success).data
                if (!list.isNullOrEmpty() && _selectedGenreIds.value.isEmpty()) {
                    toggleGenre(list.first().id)
                }
            }
        }
    }

    fun toggleGenre(genreId: Int) {
        val current = _selectedGenreIds.value.toMutableList()
        if (current.contains(genreId)) {
            if (current.size > 1) current.remove(genreId)
        } else {
            current.add(genreId)
        }
        _selectedGenreIds.value = current
    }
}
