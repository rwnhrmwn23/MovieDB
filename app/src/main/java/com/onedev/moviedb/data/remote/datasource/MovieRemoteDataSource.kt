package com.onedev.moviedb.data.remote.datasource

import com.onedev.moviedb.data.remote.api.MovieApi
import com.onedev.moviedb.data.remote.dto.GenreResponseDto
import com.onedev.moviedb.data.remote.dto.MovieDetailDto
import com.onedev.moviedb.data.remote.dto.MovieResponseDto
import com.onedev.moviedb.data.remote.dto.ReviewResponseDto
import com.onedev.moviedb.data.remote.dto.VideoResponseDto

class MovieRemoteDataSource(private val api: MovieApi) {
    suspend fun getGenres(): GenreResponseDto = api.getGenres()
    
    suspend fun getMoviesByGenre(genreIds: String, page: Int): MovieResponseDto = 
        api.getMoviesByGenre(genreIds, page)
        
    suspend fun searchMovies(query: String, page: Int): MovieResponseDto = 
        api.searchMovies(query, page)
        
    suspend fun getMovieDetail(movieId: Int): MovieDetailDto = 
        api.getMovieDetail(movieId)
        
    suspend fun getMovieReviews(movieId: Int): ReviewResponseDto = 
        api.getMovieReviews(movieId)
        
    suspend fun getMovieVideos(movieId: Int): VideoResponseDto = 
        api.getMovieVideos(movieId)
}
