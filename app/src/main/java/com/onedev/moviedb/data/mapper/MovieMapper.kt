package com.onedev.moviedb.data.mapper

import com.onedev.moviedb.data.remote.dto.GenreDto
import com.onedev.moviedb.data.remote.dto.MovieDetailDto
import com.onedev.moviedb.data.remote.dto.MovieDto
import com.onedev.moviedb.data.remote.dto.ReviewDto
import com.onedev.moviedb.data.remote.dto.VideoDto
import com.onedev.moviedb.domain.model.Genre
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.model.MovieDetail
import com.onedev.moviedb.domain.model.Review
import com.onedev.moviedb.domain.model.Video

object MovieMapper {
    
    fun mapGenreDtoToDomain(dto: GenreDto): Genre {
        return Genre(
            id = dto.id,
            name = dto.name
        )
    }

    fun mapMovieDtoToDomain(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            voteAverage = dto.voteAverage,
            releaseDate = dto.releaseDate,
            genreIds = dto.genreIds
        )
    }

    fun mapMovieDetailDtoToDomain(dto: MovieDetailDto): MovieDetail {
        return MovieDetail(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            voteAverage = dto.voteAverage,
            releaseDate = dto.releaseDate,
            genres = dto.genres.map { mapGenreDtoToDomain(it) },
            runtime = dto.runtime ?: 0,
            tagline = dto.tagline
        )
    }

    fun mapReviewDtoToDomain(dto: ReviewDto): Review {
        return Review(
            id = dto.id,
            author = dto.author,
            username = dto.authorDetails?.username ?: "",
            rating = dto.authorDetails?.rating,
            content = dto.content,
            createdAt = dto.createdAt,
            avatarPath = dto.authorDetails?.avatarPath
        )
    }

    fun mapVideoDtoToDomain(dto: VideoDto): Video {
        return Video(
            id = dto.id,
            key = dto.key,
            name = dto.name,
            site = dto.site,
            type = dto.type
        )
    }
}
