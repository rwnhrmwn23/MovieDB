package com.onedev.moviedb.domain.usecase

import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.Review
import com.onedev.moviedb.domain.repository.MovieRepository

class GetMovieReviewsUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): Resource<List<Review>> {
        return repository.getMovieReviews(movieId)
    }
}
