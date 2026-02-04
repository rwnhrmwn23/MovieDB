package com.onedev.moviedb.domain.usecase

import androidx.paging.PagingData
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesByGenreUseCase(private val repository: MovieRepository) {
    operator fun invoke(genreIds: List<Int>): Flow<PagingData<Movie>> {
        return repository.getMoviesByGenre(genreIds)
    }
}
