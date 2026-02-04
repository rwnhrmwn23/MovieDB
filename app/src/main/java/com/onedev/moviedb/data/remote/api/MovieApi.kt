package com.onedev.moviedb.data.remote.api

import com.onedev.moviedb.data.remote.dto.GenreResponseDto
import com.onedev.moviedb.data.remote.dto.MovieDetailDto
import com.onedev.moviedb.data.remote.dto.MovieResponseDto
import com.onedev.moviedb.data.remote.dto.ReviewResponseDto
import com.onedev.moviedb.data.remote.dto.VideoResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MovieApi(private val client: HttpClient) {
    
    suspend fun getGenres(): GenreResponseDto {
        return client.get("genre/movie/list").body()
    }
    
    suspend fun getMoviesByGenre(genreIds: String, page: Int): MovieResponseDto {
        return client.get("discover/movie") {
            parameter("with_genres", genreIds)
            parameter("page", page)
        }.body()
    }
    
    suspend fun searchMovies(query: String, page: Int): MovieResponseDto {
        return client.get("search/movie") {
            parameter("query", query)
            parameter("page", page)
        }.body()
    }
    
    suspend fun getMovieDetail(movieId: Int): MovieDetailDto {
        return client.get("movie/$movieId").body()
    }
    
    suspend fun getMovieReviews(movieId: Int): ReviewResponseDto {
        return client.get("movie/$movieId/reviews").body()
    }
    
    suspend fun getMovieVideos(movieId: Int): VideoResponseDto {
        return client.get("movie/$movieId/videos").body()
    }
}
