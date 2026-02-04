package com.onedev.moviedb.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponseDto(
    @SerialName("genres") val genres: List<GenreDto>
)

@Serializable
data class GenreDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

@Serializable
data class MovieResponseDto(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<MovieDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class MovieDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>
)

@Serializable
data class MovieDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("genres") val genres: List<GenreDto>,
    @SerialName("runtime") val runtime: Int?,
    @SerialName("tagline") val tagline: String?
)

@Serializable
data class ReviewResponseDto(
    @SerialName("results") val results: List<ReviewDto>
)

@Serializable
data class ReviewDto(
    @SerialName("id") val id: String,
    @SerialName("author") val author: String,
    @SerialName("content") val content: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("author_details") val authorDetails: AuthorDetailsDto? = null
)

@Serializable
data class AuthorDetailsDto(
    @SerialName("name") val name: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("avatar_path") val avatarPath: String? = null,
    @SerialName("rating") val rating: Double? = null
)

@Serializable
data class VideoResponseDto(
    @SerialName("results") val results: List<VideoDto>
)

@Serializable
data class VideoDto(
    @SerialName("id") val id: String,
    @SerialName("key") val key: String,
    @SerialName("name") val name: String,
    @SerialName("site") val site: String,
    @SerialName("type") val type: String
)
