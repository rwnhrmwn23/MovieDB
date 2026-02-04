package com.onedev.moviedb.domain.usecase

import androidx.paging.PagingData
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class SearchMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(query: String): Flow<PagingData<Movie>> {
        return repository.searchMovies(query)
    }
}
