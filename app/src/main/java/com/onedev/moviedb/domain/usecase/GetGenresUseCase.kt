package com.onedev.moviedb.domain.usecase

import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.Genre
import com.onedev.moviedb.domain.repository.MovieRepository

class GetGenresUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(): Resource<List<Genre>> {
        return repository.getGenres()
    }
}
