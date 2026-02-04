package com.onedev.moviedb.domain.usecase

import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.MovieDetail
import com.onedev.moviedb.domain.repository.MovieRepository

class GetMovieDetailUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): Resource<MovieDetail> {
        return repository.getMovieDetail(movieId)
    }
}
