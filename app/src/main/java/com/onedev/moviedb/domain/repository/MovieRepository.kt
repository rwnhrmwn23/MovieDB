package com.onedev.moviedb.domain.repository

import androidx.paging.PagingData
import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.Genre
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.model.MovieDetail
import com.onedev.moviedb.domain.model.Review
import com.onedev.moviedb.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getGenres(): Resource<List<Genre>>
    fun getMoviesByGenre(genreIds: List<Int>): Flow<PagingData<Movie>>
    fun searchMovies(query: String): Flow<PagingData<Movie>>
    suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail>
    suspend fun getMovieReviews(movieId: Int): Resource<List<Review>>
    suspend fun getMovieVideos(movieId: Int): Resource<List<Video>>
}
