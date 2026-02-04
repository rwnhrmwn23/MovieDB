package com.onedev.moviedb.domain.usecase

import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.Video
import com.onedev.moviedb.domain.repository.MovieRepository

class GetMovieVideosUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): Resource<List<Video>> {
        return repository.getMovieVideos(movieId)
    }
}
