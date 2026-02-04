package com.onedev.moviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.data.mapper.MovieMapper
import com.onedev.moviedb.data.paging.MoviePagingSource
import com.onedev.moviedb.data.remote.datasource.MovieRemoteDataSource
import com.onedev.moviedb.domain.model.Genre
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.model.MovieDetail
import com.onedev.moviedb.domain.model.Review
import com.onedev.moviedb.domain.model.Video
import com.onedev.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val remoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override suspend fun getGenres(): Resource<List<Genre>> {
        return try {
            val response = remoteDataSource.getGenres()
            val genres = response.genres.map { MovieMapper.mapGenreDtoToDomain(it) }
            Resource.Success(genres)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override fun getMoviesByGenre(genreIds: List<Int>): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(remoteDataSource, genreIds, query = null) }
        ).flow
    }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(remoteDataSource, emptyList(), query = query) }
        ).flow
    }

    override suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail> {
        return try {
            val response = remoteDataSource.getMovieDetail(movieId)
            Resource.Success(MovieMapper.mapMovieDetailDtoToDomain(response))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getMovieReviews(movieId: Int): Resource<List<Review>> {
        return try {
            val response = remoteDataSource.getMovieReviews(movieId)
            val reviews = response.results.map { MovieMapper.mapReviewDtoToDomain(it) }
            Resource.Success(reviews)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getMovieVideos(movieId: Int): Resource<List<Video>> {
        return try {
            val response = remoteDataSource.getMovieVideos(movieId)
            val videos = response.results.map { MovieMapper.mapVideoDtoToDomain(it) }
            Resource.Success(videos)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}
